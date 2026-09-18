package jp.co.sss.lms.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.lms.dto.MeetingFileDto;
import jp.co.sss.lms.form.MeetingFileListForm;
import jp.co.sss.lms.form.MeetingFileRegistForm;
import jp.co.sss.lms.form.MeetingRegistForm;
import jp.co.sss.lms.service.MeetingService;

@Controller
@RequestMapping("/meeting")
public class MeetingController {

	@Autowired
	private MeetingService meetingService;

	/**
	 * ユーザー詳細画面 『ダウンロード（面談記録）』ボタン押下
	 * 
	 * @param meetingId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(path = "/download", method = RequestMethod.POST)
	@ResponseBody
	public void download(Integer meetingId, HttpServletResponse response) throws IOException {
		meetingService.downloadMeetingFile(meetingId, response);
	}

	/**
	 * Task.46 ユーザー詳細画面 『OK（面談記録削除確認ダイアログ）』ボタン押下
	 * 
	 * @param lmsUserId
	 * @param meetingId
	 * @return ユーザー詳細画面
	 */
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	public String delete(Integer lmsUserId, Integer meetingId) {
		meetingService.deleteMeeting(meetingId);
		return "redirect:/user/detail?lmsUserId=" + lmsUserId;
	}

	/**
	 * Task.47 面談記録登録画面 初期表示
	 * 
	 * @param meetingRegistForm
	 * @return 面談記録登録画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.GET)
	public String registInit(@ModelAttribute MeetingRegistForm meetingRegistForm) {
		meetingService.setMeetingRegistForm(meetingRegistForm);
		return "meeting/regist";
	}

	/**
	 * Task.47 面談記録登録画面 『登録する』または『変更する』ボタン押下
	 * 
	 * @param meetingRegistForm
	 * @param result
	 * @param redirectAttributes
	 * @return ユーザー詳細画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String registPost(MeetingRegistForm meetingRegistForm, BindingResult result) {

		meetingService.meetingRegistInputCheck(meetingRegistForm, result);
		if (result.hasErrors()) {
			return "meeting/regist";
		}

		meetingService.registMeeting(meetingRegistForm);
		return "redirect:/user/detail?lmsUserId=" + meetingRegistForm.getLmsUserId();
	}

	/**
	 * Task.106 面談テンプレートファイル一覧画面 初期表示 または『検索』ボタン押下
	 * 
	 * @param meetingFileForm
	 * @return 面談テンプレートファイル一覧画面
	 */
	@RequestMapping(path = "/fileList")
	public String fileList(MeetingFileListForm meetingFileListForm) {
		List<MeetingFileDto> meetingFileDtoList = meetingService.getMeetingFileDtoList(null,
				meetingFileListForm.getFileName());
		meetingFileListForm.setMeetingFileDtoList(meetingFileDtoList);
		return "meeting/fileList";
	}

	/**
	 * Task.106 面談テンプレートファイル一覧画面 『削除』ボタン押下
	 * 
	 * @param meetingFileForm
	 * @param result
	 * @param redirectAttributes
	 * @return 面談テンプレートファイル一覧画面
	 */
	@RequestMapping(path = "/fileList", params = "delete", method = RequestMethod.POST)
	public String delete(MeetingFileListForm meetingFileListForm, BindingResult result,
			RedirectAttributes redirectAttributes) {

		meetingService.fileDeleteInputCheck(meetingFileListForm, result);
		if (result.hasErrors()) {
			List<MeetingFileDto> meetingFileDtoList = meetingService.getMeetingFileDtoList(null,
					meetingFileListForm.getFileName());
			meetingFileListForm.setMeetingFileDtoList(meetingFileDtoList);
			return "meeting/fileList";
		}

		String message = meetingService.delete(meetingFileListForm.getMeetingFileId());
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/meeting/fileList";
	}

	/**
	 * Task.107 面談テンプレートファイル登録画面 初期表示
	 * 
	 * @param meetingFileRegistForm
	 * @return 面談テンプレートファイル登録画面
	 */
	@RequestMapping(path = "/fileRegist", method = RequestMethod.POST)
	public String fileRegist(MeetingFileRegistForm meetingFileRegistForm) {
		meetingService.setMeetingFileRegistForm(meetingFileRegistForm);
		return "meeting/fileRegist";
	}

	/**
	 * Task.107 面談テンプレートファイル登録画面 『登録する』または『変更する』ボタン押下
	 * 
	 * @param meetingFileRegistForm
	 * @param result
	 * @param redirectAttributes
	 * @return 面談テンプレートファイル一覧画面
	 */
	@RequestMapping(path = "/fileRegist", params = "complete", method = RequestMethod.POST)
	public String fileRegistComplete(@Valid MeetingFileRegistForm meetingFileRegistForm,
			BindingResult result, RedirectAttributes redirectAttributes) {

		meetingService.fileRegistInputCheck(meetingFileRegistForm, result);
		if (result.hasErrors()) {
			return "meeting/fileRegist";
		}

		String message = meetingService.fileRegistComplete(meetingFileRegistForm);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/meeting/fileList";
	}

}
