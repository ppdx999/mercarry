package org.ppdx.mercarry.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageController extends BaseController {

	@GetMapping("/mypage")
	public String mypage(Model model) {
		return "mypage/index";
	}
}
