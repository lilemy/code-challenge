'use server';
import React from 'react';
import { listQuestionVoByPage } from '@/api/questionController';
import { Button, Card, Col, message, Row } from 'antd';
import Title from 'antd/es/typography/Title';
import QuestionTable from '@/components/QuestionTable';
import './index.css';
import Link from 'next/link';

const QuestionsPage: React.FC = async () => {
  let questionList = [];
  let total = 0;
  try {
    const questionListRes = await listQuestionVoByPage({
      pageSize: 12,
      sortField: 'createTime',
      sortOrder: 'descend',
    });
    // @ts-ignore
    questionList = questionListRes.data.records ?? [];
    // @ts-ignore
    total = questionListRes.data.total ??  0;
  } catch (e: any) {
    message.error('获取题目列表失败', e);
  }
  return (
    <div id="questionsPage" className="max-width-content">
      <Card style={{ marginBottom: 15 }}>
        <Row>
          <Col flex="auto">
            <Title level={3}>题目大全</Title>
          </Col>
          <Col>
            <Link href="/question/create">
              <Button type="primary" size={'large'}>
                新建题目
              </Button>
            </Link>
          </Col>
        </Row>
      </Card>
      <QuestionTable defaultQuestionList={questionList} defaultTotal={total} />
    </div>
  );
};

export default QuestionsPage;
