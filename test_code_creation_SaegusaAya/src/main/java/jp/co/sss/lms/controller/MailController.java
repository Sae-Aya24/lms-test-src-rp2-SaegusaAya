package jp.co.sss.lms.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.lms.dto.MailQueDto;
import jp.co.sss.lms.dto.MailTemplateDto;
import jp.co.sss.lms.service.MailService;

/**
 * メールコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/mail")
public class MailController {

	@Autowired
	private MailService mailService;

	/**
	 * Task.92 メールテンプレート選択画面 初期表示
	 * 
	 * @param model
	 * @return メールテンプレート選択画面
	 */
	@RequestMapping(path = "/sendList")
	public String templateList(Model model) {

		List<MailTemplateDto> mailTemplateDtoList = mailService.getMailTemplateDtoList();
		model.addAttribute("mailTemplateDtoList", mailTemplateDtoList);

		return "mail/sendList";
	}

	/**
	 * Task.93 メールテンプレート選択画面 『選択』ボタン押下
	 * 
	 * @param mailTemplateId
	 * @param model
	 * @return テンプレート内容確認
	 */
	@RequestMapping(path = "/sendTemplateConfirm")
	public String templateConfirm(Integer mailTemplateId, Model model) {

		MailTemplateDto mailTemplateDto = mailService.getMailTemplateDto(mailTemplateId);
		model.addAttribute("mailTemplateDto", mailTemplateDto);

		return "mail/sendTemplateConfirm";
	}

	/**
	 * Task.94 メールテンプレート選択画面 『メール送信キュー一覧』ボタン押下
	 * 
	 * @param model
	 * @return メール送信キュー一覧画面
	 */
	@RequestMapping(path = "/queList")
	public String queList(Model model) {

		List<MailQueDto> mailQueDtoList = mailService.getMailQueDtoList();
		model.addAttribute("mailQueDtoList", mailQueDtoList);

		return "mail/queList";
	}

	/**
	 * Task.94 メール送信キュー一覧画面 『送信中止』ボタン押下
	 * 
	 * @param mailQueId
	 * @param model
	 * @return メール送信キュー一覧画面
	 */
	@RequestMapping(path = "/queList", params = "delete")
	public String queDelete(Integer mailQueId, Model model) {

		String error = mailService.queDelete(mailQueId);
		if (!StringUtils.isBlank(error)) {
			model.addAttribute("error", error);
			List<MailQueDto> mailQueDtoList = mailService.getMailQueDtoList();
			model.addAttribute("mailQueDtoList", mailQueDtoList);
			return "mail/queList";
		}

		return "redirect:/mail/queList";
	}

}
