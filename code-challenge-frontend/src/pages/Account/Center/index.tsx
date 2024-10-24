import CalendarChart from '@/pages/Account/Center/components/CalendarChart';
import PersonalQuestionList from '@/pages/Account/Center/components/PersonalQuestionList';
import { listMyQuestionVoByPage } from '@/services/code-challenge/questionController';
import { getLoginUser } from '@/services/code-challenge/userController';
import { GridContent } from '@ant-design/pro-layout';
import { Card, Col, Image, message, Row } from 'antd';
import Meta from 'antd/es/card/Meta';
import Paragraph from 'antd/es/typography/Paragraph';
import Title from 'antd/es/typography/Title';
import React, { useEffect, useState } from 'react';

/**
 * 个人中心
 * @constructor
 */
const Center: React.FC = () => {
  // 当前登录用户
  const [loginUserVO, setLoginUserVO] = React.useState<API.LoginUserVO>();
  // 当前用户题目列表
  const [userQuestionList, setUserQuestionList] = React.useState<API.QuestionPersonalVO[]>([]);
  const [loading, setLoading] = React.useState(false);
  // 菜单栏
  const [activeTabKey, setActiveTabKey] = useState<string>('record');

  // 获取当前用户
  const loginUser = async () => {
    setLoading(true);
    try {
      const res = await getLoginUser();
      setLoginUserVO(res.data);
    } catch (e: any) {
      message.error(e.message);
    }
    setLoading(false);
  };

  // 获取用户创建题目
  const userQuestion = async () => {
    try {
      const res = await listMyQuestionVoByPage({
        pageSize: 12,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setUserQuestionList(res.data?.records ?? []);
    } catch (e: any) {
      message.error('获取题目失败，', e.message);
    }
  };

  // 渲染tab切换
  const renderChildrenByTabKey = (tabValue: string) => {
    if (tabValue === 'record') {
      return <CalendarChart />;
    }
    if (tabValue === 'question') {
      return <PersonalQuestionList questionList={userQuestionList} />;
    }
    return null;
  };

  useEffect(() => {
    loginUser().then(() => '');
  }, []);
  useEffect(() => {
    userQuestion().then(() => '');
  }, []);

  return (
    <GridContent className="max-width-content">
      <Row gutter={24}>
        <Col lg={6} md={24}>
          <Card loading={loading} bordered={false}>
            <div style={{ textAlign: 'center', marginBottom: '24px' }}>
              {loginUserVO?.userAvatar ? (
                <Image width={104} src={loginUserVO?.userAvatar} />
              ) : (
                <img width={104} alt="userAvatar" src="/logo.svg" />
              )}
              <Title style={{ marginTop: '20px' }} level={3}>
                {loginUserVO?.username}
              </Title>
              <Meta
                description={
                  <>
                    <Paragraph type="secondary">{loginUserVO?.userProfile}</Paragraph>
                  </>
                }
              />
            </div>
          </Card>
        </Col>
        <Col lg={18} md={24}>
          <Card
            tabList={[
              {
                key: 'record',
                label: '刷题记录',
              },
              {
                key: 'question',
                label: '创建题目',
              },
            ]}
            activeTabKey={activeTabKey}
            onTabChange={(key: string) => {
              setActiveTabKey(key);
            }}
          >
            {renderChildrenByTabKey(activeTabKey)}
          </Card>
        </Col>
      </Row>
    </GridContent>
  );
};

export default Center;
