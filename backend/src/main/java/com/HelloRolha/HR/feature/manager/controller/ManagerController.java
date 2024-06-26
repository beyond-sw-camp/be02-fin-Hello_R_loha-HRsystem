package com.HelloRolha.HR.feature.manager.controller;

import com.HelloRolha.HR.common.dto.BaseRes;
import com.HelloRolha.HR.feature.employee.model.dto.SignUp.SignUpReq;
import com.HelloRolha.HR.feature.employee.service.EmployeeService;
import com.HelloRolha.HR.feature.goout.model.Holiday;
import com.HelloRolha.HR.feature.goout.model.dto.HolidayDto;
import com.HelloRolha.HR.feature.goout.service.HolidayService;
import com.HelloRolha.HR.feature.manager.service.ManagerService;
import com.HelloRolha.HR.feature.salary.model.dto.GetDatesOfSalaryRes;
import com.HelloRolha.HR.feature.salary.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;
    private final SalaryService salaryService;
    private final HolidayService holidayService;

    @RequestMapping(method = RequestMethod.PATCH, value = "/authorize/{employeeId}")
    public ResponseEntity authorize(@PathVariable Integer employeeId) {

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("승인 성공")
                .result(managerService.authorize(employeeId))
                .build();


        return ResponseEntity.ok().body(res);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/authorize")
    public ResponseEntity findAuthorize() {

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("findAuthorize 성공")
                .result(managerService.findAuthorize())
                .build();


        return ResponseEntity.ok().body(res);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public ResponseEntity<BaseRes> check() {
        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("check Success")
                .result(null)
                .build();
        return ResponseEntity.ok().body(res);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/employee")
    public ResponseEntity<BaseRes> listEmployee() {

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("모든 직원 목록 가져오기 성공")
                .result(managerService.listEmployee())
                .build();


        return ResponseEntity.ok().body(res);
    }

    //직원 상세보기
    @RequestMapping(method = RequestMethod.GET, value = "/employee/{employeeId}")
    public ResponseEntity readEmployee(@PathVariable Integer employeeId) {

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("직원 정보 성공")
                .result(managerService.readEmployee(employeeId))
                .build();


        return ResponseEntity.ok().body(res);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/salary")
    public ResponseEntity salary() {
        LocalDate startDate =LocalDate.parse("2024-01-01");
        LocalDate endDate =LocalDate.parse("2024-01-31");
        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("직원 정보 성공")
                .result(salaryService.createSalary())
                .build();


        return ResponseEntity.ok().body(res);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/salary/init")
    public ResponseEntity salaryInit() {

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("직원 정보 성공")
                .result(salaryService.init())
                .build();


        return ResponseEntity.ok().body(res);
    }

    // 전체 내역 조회
    @RequestMapping(method = RequestMethod.GET, value = "/salary/list")
    public ResponseEntity<BaseRes> getSalaryList() {

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("전체 월급 정보 성공")
                .result(salaryService.getSalaryList())
                .build();


        return ResponseEntity.ok().body(res);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/salary/list/{year}/{month}")
    public ResponseEntity<BaseRes> getSalaryList(@PathVariable Integer year,@PathVariable Integer month) {

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message(String.format("get [ %s ]-[ %s ] salary list Request Success", year ,month))
                .result(salaryService.getSalaryList(year,month))
                .build();


        return ResponseEntity.ok().body(res);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/salary/last")
    public ResponseEntity<BaseRes> getLastestDateOfSalary() {

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("getLastDateOfSalary Request is Success")
                .result(salaryService.getLastDateOfSalary())
                .build();


        return ResponseEntity.ok().body(res);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/salary/months")
    public ResponseEntity<BaseRes> getDatesOfSalary() {
        GetDatesOfSalaryRes getDatesOfSalaryRes = GetDatesOfSalaryRes.builder()
                .lastDate(salaryService.getLastDateOfSalary())
                .firstDate(salaryService.getFirstDateOfSalary()).build();

        BaseRes res = BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("getDatesOfSalary Request is Success")
                .result(getDatesOfSalaryRes)
                .build();


        return ResponseEntity.ok().body(res);
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/attend")
//    public ResponseEntity<BaseRes> listCommute() {
//
//        BaseRes res = BaseRes.builder()
//                .code(200)
//                .isSuccess(true)
//                .message("모든 출퇴근 목록 가져오기 성공")
//                .result(managerService.listCommute())
//                .build();
//
//
//        return ResponseEntity.ok().body(res);
//    }

    // Todo 오늘 것만 가져오기 sql문을 복잡하게 써서 할까?
//    @RequestMapping(method = RequestMethod.GET, value = "/attend/check")
//    public ResponseEntity<BaseRes> checkToday() {
//
//        BaseRes res = BaseRes.builder()
//                .code(200)
//                .isSuccess(true)
//                .message("모든 출퇴근 목록 가져오기 성공")
//                .result(managerService.checkToday())
//                .build();
//
//
//        return ResponseEntity.ok().body(res);
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/holiday/create")
    public ResponseEntity<BaseRes> addHoliday(@RequestBody HolidayDto holidayDto) {
        Holiday holiday = holidayService.addHoliday(holidayDto.getDate(), holidayDto.getDescription());
        return ResponseEntity.ok(BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("공휴일 추가 성공")
                .result(holiday)
                .build());
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/holiday/update")
    public ResponseEntity<BaseRes> updateHoliday(@PathVariable Integer id, @RequestBody HolidayDto holidayDto) {
        Holiday updatedHoliday = holidayService.updateHoliday(id, holidayDto.getDate(), holidayDto.getDescription());
        return ResponseEntity.ok(BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("공휴일 수정 성공")
                .result(updatedHoliday)
                .build());
    }

    // 공휴일 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/holiday/delete/{id}")
    public ResponseEntity<BaseRes> deleteHoliday(@PathVariable Integer id) {
        holidayService.deleteHoliday(id);
        return ResponseEntity.ok(BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("공휴일 삭제 성공")
                .build());
    }

    // 전체 공휴일 목록 조회
    @RequestMapping(method = RequestMethod.GET, value = "/holiday/list")
    public ResponseEntity<BaseRes> findAllHolidays() {
        List<Holiday> holidays = holidayService.findAllHolidays();
        return ResponseEntity.ok(BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("전체 공휴일 목록 조회 성공")
                .result(holidays)
                .build());
    }

    // 공휴일 상세 조회
    @RequestMapping(method = RequestMethod.GET, value = "/holiday/{id}")
    public ResponseEntity<BaseRes> findHolidayById(@PathVariable Integer id) {
        Holiday holiday = holidayService.findHolidayById(id);
        return ResponseEntity.ok(BaseRes.builder()
                .code(200)
                .isSuccess(true)
                .message("공휴일 상세 조회 성공")
                .result(holiday)
                .build());
    }
}
