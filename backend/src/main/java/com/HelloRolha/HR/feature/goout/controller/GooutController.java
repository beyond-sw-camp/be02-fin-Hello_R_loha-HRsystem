package com.HelloRolha.HR.feature.goout.controller;

import com.HelloRolha.HR.common.dto.BaseRes;
import com.HelloRolha.HR.feature.goout.model.Goout;
import com.HelloRolha.HR.feature.goout.model.GooutFile;
import com.HelloRolha.HR.feature.goout.model.dto.*;
import com.HelloRolha.HR.feature.goout.service.GooutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/goout")
@CrossOrigin("*")
public class GooutController {
    private final GooutService gooutService;

    public GooutController(GooutService gooutService) {
        this.gooutService = gooutService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity create(@RequestPart GooutCreateReq gooutCreateReq,
                                 @RequestPart(name = "uploadFiles", required = false) MultipartFile[] uploadFiles) {
        GooutCreateRes gooutCreateRes = gooutService.create(gooutCreateReq);

        if (uploadFiles != null) {
            for (MultipartFile uploadFile : uploadFiles) {
                // 파일 업로드 메소드 호출 시 gooutId 전달
                String uploadPath = gooutService.uploadFile(uploadFile, gooutCreateRes.getGooutId());
            }
        }

        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("휴가/외출 신청 성공")
                .isSuccess(true)
                .result(gooutCreateRes.getGooutId())
                .build();
        return ResponseEntity.ok().body(response);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public ResponseEntity<BaseRes> list(Integer page, Integer size) {
        GooutListRes gooutListRes = gooutService.list(page, size);
        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("휴가/외출 확인 성공")
                .isSuccess(true)
                .result(gooutListRes)
                .build();
        return ResponseEntity.ok(response);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/check/{id}")
    public ResponseEntity<BaseRes> read(@PathVariable Integer id) {
        GooutRead gooutRead = gooutService.read(id); // 서비스에서 GooutRead 객체 받기
        if (gooutRead == null) {
            return ResponseEntity.notFound().build(); // 적절한 예외 처리
        }

        BaseRes response = BaseRes.builder() // 응답 객체 생성
                .code(1200)
                .message("휴가/외출 상세 확인 성공")
                .isSuccess(true)
                .result(gooutRead)
                .build();
        return ResponseEntity.ok(response);
    }


    @RequestMapping(method = RequestMethod.PATCH, value = "/return")
    public ResponseEntity<BaseRes> returnStatus(@RequestBody GooutReturnReq gooutReturnReq) {
        gooutService.returnStatus(gooutReturnReq);
        String message;

        if (gooutReturnReq.getStatus() == 1) {
            message = "결재자1 휴가/외출 승인 성공";
        } else if (gooutReturnReq.getStatus() == 2) {
            message = "결재자2 휴가/외출 승인 성공";
        } else if (gooutReturnReq.getStatus() == 3) {
            message = "휴가/외출 반려 성공";
        } else {
            message = "잘못된 상태 값";
        }

        BaseRes response = BaseRes.builder()
                .code(1200)
                .message(message)
                .isSuccess(true)
                .build();
        return ResponseEntity.ok(response);
    }


    @RequestMapping(method = RequestMethod.PATCH, value = "/update")
    public ResponseEntity<BaseRes> update(@RequestBody GooutUpdateReq gooutUpdateReq) {
        gooutService.update(gooutUpdateReq);
        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("휴가/외출 정보 수정 성공")
                .isSuccess(true)
                .result(gooutUpdateReq)
                .build();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
    public ResponseEntity<BaseRes> delete(@PathVariable Integer id) {
        gooutService.delete(id);
        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("휴가/외출 정보 삭제 성공")
                .isSuccess(true)
                .result("삭제한 id : " + id)
                .build();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/cancel/{id}")
    public ResponseEntity<BaseRes> cancel(@PathVariable Integer id) {
        gooutService.cancel(id);
        BaseRes response = BaseRes.builder()
                .code(1200)
                .message("휴가/외출 정보 삭제 성공")
                .isSuccess(true)
                .result("결재취소한 id : " + id)
                .build();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/files/{gooutId}")
    public ResponseEntity<List<GooutFileDto>> listFilesByGooutId(@PathVariable Integer gooutId) {
        List<GooutFileDto> files = gooutService.listFilesByGooutId(gooutId);
        return ResponseEntity.ok().body(files);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/remainingVacationDays")
    public ResponseEntity<BaseRes> calculateRemainingVacationDays(@RequestParam Integer employeeId,
                                                                  @RequestParam Integer gooutTypeId) {
        try {
            int remainingDays = gooutService.calculateRemainingVacationDays(employeeId, gooutTypeId);
            return ResponseEntity.ok(BaseRes.builder()
                    .code(1200)
                    .message("남은 휴가 일수 조회 성공")
                    .isSuccess(true)
                    .result(remainingDays)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(BaseRes.builder()
                    .code(1400)
                    .message("남은 휴가 일수 조회 실패: " + e.getMessage())
                    .isSuccess(false)
                    .build());
        }
    }
}