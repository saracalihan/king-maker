package com.example.tahakkum.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.tahakkum.constant.OTPStatus;
import com.example.tahakkum.constant.OTPTypes;
import com.example.tahakkum.dto.otp.request.OTPVerifyDto;
import com.example.tahakkum.dto.otp.request.RegisterAppDto;
import com.example.tahakkum.dto.otp.request.TOTPCreateDto;
import com.example.tahakkum.dto.otp.response.OtpDto;
import com.example.tahakkum.dto.otp.response.TotpDto;
import com.example.tahakkum.exception.ResponseException;
import com.example.tahakkum.model.OTPApp;
import com.example.tahakkum.model.OTPCode;
import com.example.tahakkum.model.Token;
import com.example.tahakkum.service.OTPService;
import com.example.tahakkum.service.TokenService;
import com.example.tahakkum.utility.Common;
import com.example.tahakkum.utility.UIBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController()
@RequestMapping("/otp")
@Tag(name = "One Time Password", description = "OTP and TOTP controller")
public class OneTimePasswordController {

    @Autowired
    private OTPService otpService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private Common common;

    private final RestTemplate restTemplate;

    public OneTimePasswordController(RestTemplate resTemp) {
        this.restTemplate = resTemp;
    }

    @Operation(
        summary = "Create OTP application",
        description = "You should to create an application for create and verify OTP code. You need to store `id` and `secret` values for create code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful Operation"),
        @ApiResponse(responseCode = "400", description = "Invalid redirect url or method"),
        @ApiResponse(responseCode = "401", description = "Invalid access token"),
    })
    @PostMapping("/register-app")
    public OTPApp createApp(@Valid @RequestBody(required = true) RegisterAppDto createDto,
            @RequestHeader(name = "x-access-token") String token) throws ResponseException {
        Optional<Token> accessToken = tokenService.findTokenByValue(token);
        if (accessToken.isEmpty()) {
            throw new ResponseException("unouthorized!", HttpStatus.UNAUTHORIZED);
        }
        // validate urls
        if (!checkUrl(createDto.getCodeRedirectUrl())) {
            throw new ResponseException("Incorrect 'codeRedirectUrl' address", HttpStatus.BAD_REQUEST);
        }
        if (!checkUrl(createDto.getRedirectUrl())) {
            throw new ResponseException(
                    "Incorrect 'redirectUrl' address", HttpStatus.BAD_REQUEST);
        }
        if (!checkUrl(createDto.getFailRedirectUrl())) {
            throw new ResponseException(
                    "Incorrect 'failRedirectUrl' address", HttpStatus.BAD_REQUEST);
        }
        String[] methods = {
                HttpMethod.GET.toString(),
                HttpMethod.POST.toString(),
                HttpMethod.PUT.toString(),
                HttpMethod.DELETE.toString(),
        };

        if (!Arrays.asList(methods).contains(createDto.getCodeRedirectMethod().toUpperCase())) {
            throw new ResponseException(
                    "Incorrect 'codeRedirectMethod' address", HttpStatus.BAD_REQUEST);
        }
        // validate code method, body and
        // variables(__id__|__code__|__secret__|__validateUrl__)
        // save

        return otpService.createApp(createDto, accessToken.get().getUser());
    }

    @Operation(summary = "Create OTP code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successful Operation"),
        @ApiResponse(responseCode = "400", description = "OTP code created but code redirect operation is failed!"),
        @ApiResponse(responseCode = "401", description = "Invalid `client_id` or `client_secret`"),
    })
    @PostMapping()
    public ResponseEntity<Object> createOtpCode(
            @RequestParam(name = "client_id", required = true) String clientId,
            @RequestParam(name = "client_secret", required = true) String clientSecret,
            @RequestBody(required = false) HashMap<String, Object> metadata) throws ResponseException {

        OTPApp app = otpService.findApp(clientId, clientSecret);
        if (app == null) {
            throw new ResponseException("unauthorized!", HttpStatus.UNAUTHORIZED);
        }

        // create code
        OTPCode code = otpService.createOtpCode(app, metadata);
        OtpDto resDto = new OtpDto();
        resDto.id = code.getOtpId();
        resDto.secret = code.getOtpSecret();
        resDto.validateUrl = common.generateOTPValidateUrl(code.getOtpId());

        try {
            app = otpService.replaceOtpCodeVariables(app, code);
            // // generate code redirect url
            // UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(app.getCodeRedirectUrl());
            // for (Entry<String, String> query : app.getCodeRedirectQueries().entrySet()) {
            //     urlBuilder = urlBuilder.queryParam(query.getKey(), query.getValue());
            // }
            // String urlWithParams = urlBuilder.toUriString();

            // generate code redirect header
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> multiHead = new LinkedMultiValueMap<>();

            for (Entry<String, String> header : app.getCodeRedirectHeader().entrySet()) {
                multiHead.add(header.getKey(), header.getValue());
            }
            headers.addAll(multiHead);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(app.getCodeRedirectBody(), headers);

            // SEND CODE REQUEST
            restTemplate.exchange(app.getCodeRedirectUrl(), HttpMethod.valueOf(app.getCodeRedirectMethod()), entity, String.class);

        } catch (Exception e) {
            System.err.println(e);
            // delete the code
            otpService.deleteCode(code);

            HashMap<String, Object> errorRes = new HashMap<>();
            errorRes.put("status", "500");
            errorRes.put("message", e.getMessage());

            if(e instanceof HttpClientErrorException || e instanceof HttpServerErrorException){
                errorRes.put("status", ((RestClientResponseException) e).getStatusCode().value());
            }
            return ResponseEntity.status(400).body(errorRes);
        }
        return ResponseEntity.status(201).body(resDto);
    }

    @Operation(summary = "Get OTP code verification UI")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful Operation"),
        @ApiResponse(responseCode = "404", description = "invalid `id`"),
    })
    @GetMapping("/verify/{id}")
    public String createotpUI(@PathVariable("id") String id) throws ResponseException {
        OTPCode code = otpService.findCodeByOtpId(id);
        if(code==null){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404));
        }
        if(!code.getStatus().equals(OTPStatus.Active.toString())){
            throw new ResponseException("", HttpStatus.BAD_REQUEST);
        }
        OTPApp app = code.getApp();
        // TODO: replace otp variables on redirect and fail url

        String res = UIBuilder.getInstance().createOTP(app.getName(), code.getOtpId(), code.getOtpSecret(), app.getRedirectUrl(), app.getFailRedirectUrl());
        return res;
    }

    @Operation(summary = "Verify OTP code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful Operation"),
        @ApiResponse(responseCode = "400", description = "The code is invalid!"),
        @ApiResponse(responseCode = "401", description = "Invalid `id` or `secret`"),
    })
    @PostMapping("/verify")
    public OTPCode validateCode(@RequestBody() @Valid OTPVerifyDto body) {
        OTPCode code = otpService.findCodeByOtpId(body.id);
        if(code == null || !code.getOtpSecret().equals(body.secret) || code.getType() != OTPTypes.OTP){
            throw new ResponseStatusException(HttpStatusCode.valueOf(401));
        }

        Boolean isSuccess = code.getCode() == body.code;
        code.setStatus(isSuccess ? OTPStatus.Success.toString() : OTPStatus.Fail.toString());
        code.setUpdatedAt(LocalDateTime.now());
        otpService.getOTPCodeRepository().save(code);

        if(!isSuccess){
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));
        }

        return code;
    }

    @Operation(summary = "Create TOTP code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successful Operation"),
        @ApiResponse(responseCode = "400", description = "OTP code created but code redirect operation is failed!"),
        @ApiResponse(responseCode = "401", description = "Invalid `client_id` or `client_secret`"),
    })
    @PostMapping("/time-based")
    public ResponseEntity<Object> createTotp(@RequestParam(name = "client_id", required = true) String clientId,
    @RequestParam(name = "client_secret", required = true) String clientSecret,
    @RequestBody(required = true) @Valid TOTPCreateDto createDto) throws ResponseException {
        OTPApp app = otpService.findApp(clientId, clientSecret);
        if (app == null) {
            throw new ResponseException("unauthorized!", HttpStatus.UNAUTHORIZED);
        }

        // create code
        OTPCode code = otpService.createTotpCode(app, createDto.time, createDto.metadatas);
        TotpDto resDto = new TotpDto();
        resDto.id = code.getOtpId();
        resDto.secret = code.getOtpSecret();
        resDto.validateUrl = common.generateTOTPValidateUrl(code.getOtpId());
        resDto.expireAt = code.getExpiredAt();

        try {
            app = otpService.replaceOtpCodeVariables(app, code);
            // // generate code redirect url
            // UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(app.getCodeRedirectUrl());
            // for (Entry<String, String> query : app.getCodeRedirectQueries().entrySet()) {
            //     urlBuilder = urlBuilder.queryParam(query.getKey(), query.getValue());
            // }
            // String urlWithParams = urlBuilder.toUriString();

            // generate code redirect header
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> multiHead = new LinkedMultiValueMap<>();

            for (Entry<String, String> header : app.getCodeRedirectHeader().entrySet()) {
                multiHead.add(header.getKey(), header.getValue());
            }
            headers.addAll(multiHead);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(app.getCodeRedirectBody(), headers);

            // SEND CODE REQUEST
            restTemplate.exchange(app.getCodeRedirectUrl(), HttpMethod.valueOf(app.getCodeRedirectMethod()), entity, String.class);

        } catch (Exception e) {
            System.err.println(e);
            // delete the code
            otpService.deleteCode(code);

            HashMap<String, Object> errorRes = new HashMap<>();
            errorRes.put("status", "500");
            errorRes.put("message", e.getMessage());

            if(e instanceof HttpClientErrorException || e instanceof HttpServerErrorException){
                errorRes.put("status", ((RestClientResponseException) e).getStatusCode().value());
            }
            return ResponseEntity.status(400).body(errorRes);
        }
        return ResponseEntity.status(201).body(resDto);
    }

    @Operation(summary = "Verify TOTP code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful Operation"),
        @ApiResponse(responseCode = "400", description = "The `code` is invalid or expired!"),
        @ApiResponse(responseCode = "401", description = "Invalid `id` or `secret`"),
    })
    @PostMapping("/time-based/verify")
    public OTPCode validateTotpCode(@RequestBody() @Valid OTPVerifyDto body) {
        OTPCode code = otpService.findCodeByOtpId(body.id);
        if(code == null || !code.getOtpSecret().equals(body.secret) || code.getType() != OTPTypes.TOTP){
            throw new ResponseStatusException(HttpStatusCode.valueOf(401));
        }

        Boolean isSuccess = code.getCode() == body.code && code.getExpiredAt().isAfter(LocalDateTime.now());
        code.setStatus(isSuccess ? OTPStatus.Success.toString() : OTPStatus.Fail.toString());
        code.setUpdatedAt(LocalDateTime.now());
        otpService.getOTPCodeRepository().save(code);

        if(!isSuccess){
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));
        }
        return code;
    }

    @Operation(summary = "Get TOTP code verification UI")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful Operation"),
        @ApiResponse(responseCode = "404", description = "invalid `id`"),
    })
    @GetMapping("/time-based/verify/{id}")
    public String createTotpUI(@PathVariable("id") String id) throws ResponseException {
        OTPCode code = otpService.findCodeByOtpId(id);
        if(code==null){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404));
        }
        if(!code.getStatus().equals(OTPStatus.Active.toString())){
            throw new ResponseException("", HttpStatus.BAD_REQUEST);
        }
        OTPApp app = code.getApp();
        // TODO: replace otp variables on redirect and fail url
        String res = UIBuilder.getInstance().createTOTP(app.getName(), code.getOtpId(), code.getOtpSecret(), app.getRedirectUrl(), app.getFailRedirectUrl(), code.getExpiredAt());
        return res;
    }

    private Boolean checkUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return false;
        }

        return true;
    }
}
