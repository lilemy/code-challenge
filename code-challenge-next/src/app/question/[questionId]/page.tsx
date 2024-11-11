"use server";
import React from "react";
import { getQuestionVoById } from "@/api/questionController";
import { message } from "antd";
import QuestionCard from "@/components/QuestionCard";

const QuestionPage = async ({ params }: { params: { questionId: any } }) => {
  const { questionId } = params;
  // 获取题目详情
  let question = undefined;
  try {
    const res = await getQuestionVoById({
      id: questionId,
    });
    question = res.data as API.QuestionVO;
  } catch (e: any) {
    message.error("获取题目详情失败，" + e.message);
  }
  // 错误处理
  if (!question) {
    return <div>获取题目详情失败，请刷新重试</div>;
  }
  return (
    <div id="questionPage" className="max-width-content">
      <QuestionCard question={question} />
    </div>
  );
};

export default QuestionPage;
