"use server";
import React from "react";
import { getQuestionBankVoById } from "@/api/questionBankController";
import { Avatar, Button, Card, message } from "antd";
import Title from "antd/es/typography/Title";
import Paragraph from "antd/es/typography/Paragraph";
import Meta from "antd/es/card/Meta";
import QuestionList from "@/components/QuestionList";
import "./index.css";

/**
 * 题库详情页
 * @constructor
 */
//@ts-ignore
const BankPage: React.FC = async ({ params }) => {
  const { questionBankId } = params;
  let bank = undefined;

  try {
    const bankRes = await getQuestionBankVoById({
      id: questionBankId,
      isNeedQueryQuestionList: true,
    });
    bank = bankRes.data as API.QuestionBankVO;
  } catch (e: any) {
    message.error("获取题库详情失败，" + e.message);
  }

  if (!bank) {
    return <div>获取题库详情失败，请刷新重试</div>;
  }

  // 获取第一道题目，用于 “开始刷题” 按钮跳转
  let firstQuestionId;
  if (bank.questionPage?.records && bank.questionPage.records.length > 0) {
    firstQuestionId = bank.questionPage.records[0].id;
  }

  return (
    <div id="bankPage" className="max-width-content">
      <Card>
        <Meta
          avatar={<Avatar src={bank?.picture} size={72} />}
          title={
            <Title level={3} style={{ marginBottom: 0 }}>
              {bank?.title}
            </Title>
          }
          description={
            <>
              <Paragraph type="secondary">{bank?.description}</Paragraph>
              <Button
                type="primary"
                shape="round"
                href={`/bank/${questionBankId}/question/${firstQuestionId}`}
                target="_blank"
                disabled={!firstQuestionId}
              >
                开始刷题
              </Button>
            </>
          }
        />
      </Card>
      <div style={{ marginBottom: 16 }} />
      <QuestionList
        questionBankId={questionBankId}
        questionList={bank.questionPage?.records ?? []}
        cardTitle={`题目列表（${bank.questionPage?.total || 0}）`}
      />
    </div>
  );
};

export default BankPage;
