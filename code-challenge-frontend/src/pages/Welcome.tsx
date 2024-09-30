import QuestionBankList from '@/components/QuestionBankList';
import QuestionList from '@/components/QuestionList';
import { listQuestionBankVoByPage } from '@/services/code-challenge/questionBankController';
import { listQuestionVoByPage } from '@/services/code-challenge/questionController';
import { Link } from '@@/exports';
import { Card, Divider, Flex, message } from 'antd';
import Title from 'antd/lib/typography/Title';
import React, { useEffect, useState } from 'react';

/**
 * 主页
 * @constructor
 */
const Welcome: React.FC = () => {
  // 题库列表
  const [questionBankList, setQuestionBankList] = useState<API.QuestionBankVO[]>([]);
  // 题目列表
  const [questionList, setQuestionList] = useState<API.QuestionVO[]>([]);
  const [loading, setLoading] = useState(false);

  const loadData = async () => {
    setLoading(true);
    // 获取题库
    try {
      const res = await listQuestionBankVoByPage({
        pageSize: 12,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setQuestionBankList(res.data?.records ?? []);
    } catch (e: any) {
      message.error('获取题库列表失败，' + e.message);
    }
    // 获取题目
    try {
      const res = await listQuestionVoByPage({
        pageSize: 12,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setQuestionList(res.data?.records ?? []);
    } catch (e: any) {
      message.error('获取题目列表失败，' + e.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData().then(() => '');
  }, []);
  return (
    <Card loading={loading} className="max-width-content">
      <Flex justify="space-between" align="center">
        <Title level={3}>最新题库</Title>
        <Link to={'/banks'}>查看更多</Link>
      </Flex>
      <QuestionBankList questionBankList={questionBankList} />
      <Divider />
      <Flex justify="space-between" align="center">
        <Title level={3}>最新题目</Title>
        <Link to={'/questions'}>查看更多</Link>
      </Flex>
      <QuestionList questionList={questionList} />
    </Card>
  );
};

export default Welcome;
