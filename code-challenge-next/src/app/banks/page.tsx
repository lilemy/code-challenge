"use server";
import React from "react";
import { message } from "antd";
import { listQuestionBankVoByPage } from "@/api/questionBankController";
import Title from "antd/es/typography/Title";
import "./index.css";
import QuestionBankList from "@/components/QuestionBankList";

const BanksPage: React.FC = async () => {
  let questionBankList = [];
  const pageSize = 200;
  try {
    const questionBankRes = await listQuestionBankVoByPage({
      pageSize,
      sortField: "createTime",
      sortOrder: "descend",
    });
    //@ts-ignore
    questionBankList = questionBankRes.data.records ?? [];
  } catch (e: any) {
    message.error("获取题库列表失败", e.message);
  }
  return (
    <div id="banksPage" className="max-width-content">
      <Title level={3}>题库大全</Title>
      <QuestionBankList questionBankList={questionBankList} />
    </div>
  );
};

export default BanksPage;
