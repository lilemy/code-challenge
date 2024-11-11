import type { Metadata } from "next";
import { Card, Divider, Flex, message } from "antd";
import { listQuestionBankVoByPage } from "@/api/questionBankController";
import { listQuestionVoByPage } from "@/api/questionController";
import Title from "antd/es/typography/Title";
import Link from "next/link";
import QuestionBankList from "@/components/QuestionBankList";
import QuestionList from "@/components/QuestionList";
import "./index.css";

export const metadata: Metadata = {
  title: "小新知识分享平台",
  description: "小新知识分享平台",
};

export default async function HomePage() {
  let questionBankList = [];
  let questionList = [];
  try {
    const questionBankRes = await listQuestionBankVoByPage({
      pageSize: 12,
      sortField: "createTime",
      sortOrder: "descend",
    });
    // @ts-ignore
    questionBankList = questionBankRes.data.records ?? [];
  } catch (e: any) {
    message.error("获取题库列表失败", e);
  }

  try {
    const questionListRes = await listQuestionVoByPage({
      pageSize: 12,
      sortField: "createTime",
      sortOrder: "descend",
    });
    // @ts-ignore
    questionList = questionListRes.data.records ?? [];
  } catch (e: any) {
    message.error("获取题目列表失败", e);
  }

  return (
    <div id="homePage" className="max-width-content">
      <Card>
        <Flex justify="space-between" align="center">
          <Title level={3}>最新题库</Title>
          <Link href={"/banks"}>查看更多</Link>
        </Flex>
        <QuestionBankList questionBankList={questionBankList} />
        <Divider />
        <Flex justify="space-between" align="center">
          <Title level={3}>最新题目</Title>
          <Link href={"/questions"}>查看更多</Link>
        </Flex>
        <QuestionList questionList={questionList} />
      </Card>
    </div>
  );
}
