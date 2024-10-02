import TagList from '@/components/TagList';
import { listQuestionVoByPage } from '@/services/code-challenge/questionController';
import { Link } from '@@/exports';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import Title from 'antd/es/typography/Title';
import React, { useRef } from 'react';

const Question: React.FC = () => {
  const actionRef = useRef<ActionType>();
  /**
   * 表格列配置
   */
  const columns: ProColumns<API.QuestionVO>[] = [
    {
      title: '标题',
      dataIndex: 'title',
      valueType: 'text',
      render: (_, record) => {
        return <Link to={`/question/${record.id}`}>{record.title}</Link>;
      },
    },
    {
      title: '标签',
      dataIndex: 'tags',
      valueType: 'select',
      fieldProps: {
        mode: 'tags',
      },
      render: (_, record) => {
        return <TagList tagList={record.tagList} />;
      },
    },
  ];
  return (
    <div className="max-width-content" style={{ width: '100%' }}>
      <Title level={3}>题目大全</Title>
      <ProTable<API.Question, API.PageQuestion>
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 'auto',
        }}
        options={false}
        size={'large'}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await listQuestionVoByPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.QuestionQueryRequest);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.total) || 0,
          };
        }}
        columns={columns}
      />
    </div>
  );
};

export default Question;
