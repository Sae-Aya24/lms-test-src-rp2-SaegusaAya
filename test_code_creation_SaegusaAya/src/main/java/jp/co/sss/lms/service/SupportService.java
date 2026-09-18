package jp.co.sss.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.lms.dto.OperatorDto;
import jp.co.sss.lms.mapper.MOperatorMapper;
import jp.co.sss.lms.util.Constants;

/**
 * Task.41 サポート情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class SupportService {

	@Autowired
	private MOperatorMapper mOperatorMapper;

	/**
	 * Task.41 オペレーター情報取得
	 * 
	 * @param operatorType
	 * @return オペレーターDTOリスト
	 */
	public List<OperatorDto> getOperator(short operatorType) {

		List<OperatorDto> operatorDtoList = mOperatorMapper.findByOperatorType(operatorType,
				Constants.DB_FLG_FALSE);

		return operatorDtoList;

	}

}
