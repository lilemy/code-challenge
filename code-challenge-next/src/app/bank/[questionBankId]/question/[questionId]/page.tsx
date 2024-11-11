"use server";
import React from "react";
import "./index.css";
import { getQuestionVoById } from "@/api/questionController";
import Link from "next/link";
import { getQuestionBankVoById } from "@/api/questionBankController";
import { Flex, Menu } from "antd";
import Sider from "antd/es/layout/Sider";
import Title from "antd/es/typography/Title";
import { Content } from "antd/es/layout/layout";
import QuestionCard from "@/components/QuestionCard";

/**
 * 题库题目详情页
 * @constructor
 */
const BankQuestionPage = async ({
  params,
}: {
  params: { questionBankId: any; questionId: any };
}) => {
  const { questionBankId, questionId } = params;
  // 获取题库详情
  let bank = undefined;
  try {
    const res = await getQuestionBankVoById({
      id: questionBankId,
      isNeedQueryQuestionList: true,
    });
    bank = res.data as API.QuestionBankVO;
  } catch (e: any) {
    console.error("获取题库列表失败，" + e.message);
  }
  // 错误处理
  if (!bank) {
    return <div>获取题库详情失败，请刷新重试</div>;
  }

  // 获取题目详情
  let question = undefined;
  try {
    const res = await getQuestionVoById({
      id: questionId,
    });
    question = res.data as API.QuestionVO;
  } catch (e: any) {
    console.error("获取题目详情失败，" + e.message);
  }
  // 错误处理
  if (!question) {
    return <div>获取题目详情失败，请刷新重试</div>;
  }

  // 题目菜单列表
  const questionMenuItemList = (bank.questionPage?.records || []).map((q) => {
    return {
      label: (
        <Link href={`/bank/${questionBankId}/question/${q.id}`}>{q.title}</Link>
      ),
      key: q.id,
    };
  });

  return (
    <div id="bankQuestionPage">
      <Flex gap={24}>
        <Sider width={240} theme="light" style={{ padding: "24px 0" }}>
          <Title level={4} style={{ padding: "0 20px" }}>
            {bank.title}
          </Title>
          <Menu
            items={questionMenuItemList as any}
            selectedKeys={[question.id?.toString() || "0"]}
          />
        </Sider>
        <Content>
          <QuestionCard question={question} />
        </Content>
      </Flex>
    </div>
  );
};

export default BankQuestionPage;
