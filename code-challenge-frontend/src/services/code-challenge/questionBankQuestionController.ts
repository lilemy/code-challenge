// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 创建题库题目关联 POST /questionBankQuestion/add */
export async function addQuestionBankQuestion(
  body: API.QuestionBankQuestionAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong>('/questionBankQuestion/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 移除题库题目关联 POST /questionBankQuestion/remove */
export async function removeQuestionBankQuestion(
  body: API.QuestionBankQuestionRemoveRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/questionBankQuestion/remove', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
