package shagiev.carwash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import shagiev.carwash.dto.user.operator.OperatorInfoDto;
import shagiev.carwash.dto.user.operator.OperatorRequestDto;
import shagiev.carwash.service.entry.IncomeService;
import shagiev.carwash.service.exceptions.NoSuchIdException;
import shagiev.carwash.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final IncomeService incomeService;
    private final String dateRegExp = "\\d{4}-[0,1]\\d-[0-3]\\d";

    @PostMapping("/operator")
    public OperatorInfoDto makeUserOperator(@RequestBody @Valid OperatorRequestDto operatorRequestDto) {
        try {
            return userService.makeUserOperator(operatorRequestDto);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/operator")
    public OperatorInfoDto updateOperator(
            @RequestBody @Valid OperatorRequestDto operatorRequestDto) {
        try {
            return userService.updateOperator(operatorRequestDto);
        } catch (NoSuchIdException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/income")
    public Long getIncome(@RequestParam(required = false) @Pattern(regexp = "\\d{4}-[0,1]\\d-[0-3]\\d") String from,
                          @RequestParam(required = false) @Pattern(regexp = "\\d{4}-[0,1]\\d-[0-3]\\d") String until) {
        return incomeService.countIncome(from, until);
    }

}
