import MdEditor from '@/components/Markdown/MdEditor';
import TagList from '@/components/TagList';
import CreateForm from '@/pages/Admin/Question/components/CreateForm';
import UpdateBankModal from '@/pages/Admin/Question/components/UpdateBankModal';
import UpdateForm from '@/pages/Admin/Question/components/UpdateForm';
import { deleteQuestion, listQuestionByPage } from '@/services/code-challenge/questionController';
import { PlusOutlined } from '@ant-design/icons';
import { type ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { Button, message, Space, Typography } from 'antd';
import React, { useRef, useState } from 'react';

const QuestionTableList: React.FC = () => {
  // 是否显示新建窗口
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  // 是否显示更新窗口
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  // 是否显示更新所属题库的弹窗
  const [updateBankModalVisible, setUpdateBankModalVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  // 当前题目的数据
  const [currentRow, setCurrentRow] = useState<API.Question>();

  /**
   * @zh-CN 删除题目
   *
   * @param question
   */
  const handleDelete = async (question: API.Question) => {
    const hide = message.loading('正在删除');
    if (!question) return true;
    try {
      await deleteQuestion({
        id: question.id,
      });
      hide();
      message.success('删除成功');
      actionRef?.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('删除失败' + error.message);
      return false;
    }
  };

  const columns: ProColumns<API.Question>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInForm: true,
    },
    {
      title: '所属题库',
      dataIndex: 'questionBankId',
      hideInTable: true,
      hideInForm: true,
    },
    {
      title: '标题',
      dataIndex: 'title',
      valueType: 'text',
    },
    {
      title: '内容',
      dataIndex: 'content',
      valueType: 'text',
      hideInSearch: true,
      width: 640,
      // @ts-ignore
      renderFormItem: (_, { fieldProps }, form) => {
        // 编写要渲染的表单项
        console.log(form);
        // value 和 onchange 会通过 form 自动注入
        return <MdEditor {...fieldProps} />;
      },
    },
    {
      title: '答案',
      dataIndex: 'answer',
      valueType: 'text',
      hideInSearch: true,
      width: 640,
      // @ts-ignore
      renderFormItem: (_, { fieldProps }, form) => {
        // 编写要渲染的表单项
        console.log(form);
        // value 和 onchange 会通过 form 自动注入
        return <MdEditor {...fieldProps} />;
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
        const tagList = JSON.parse(record.tags || '[]');
        return <TagList tagList={tagList} />;
      },
    },

    {
      title: '创建用户',
      dataIndex: 'userId',
      valueType: 'text',
      hideInForm: true,
    },

    {
      title: '创建时间',
      sorter: true,
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '编辑时间',
      sorter: true,
      dataIndex: 'editTime',
      valueType: 'dateTime',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '更新时间',
      sorter: true,
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => (
        <Space size="middle">
          <Typography.Link
            onClick={() => {
              setCurrentRow(record);
              setUpdateBankModalVisible(true);
            }}
          >
            修改所属题库
          </Typography.Link>
          <Typography.Link
            onClick={() => {
              setCurrentRow(record);
              setUpdateModalVisible(true);
            }}
          >
            修改
          </Typography.Link>
          <Typography.Link type="danger" onClick={() => handleDelete(record)}>
            删除
          </Typography.Link>
        </Space>
      ),
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.Question, API.PageQuestion>
        headerTitle={'题目信息'}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        scroll={{ x: 1200 }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              setCreateModalVisible(true);
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await listQuestionByPage({
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
      <CreateForm
        modalVisible={createModalVisible}
        columns={columns}
        onSubmit={() => {
          setCreateModalVisible(false);
          actionRef.current?.reload();
        }}
        onCancel={() => {
          setCreateModalVisible(false);
        }}
      />
      <UpdateForm
        modalVisible={updateModalVisible}
        columns={columns}
        oldData={currentRow}
        onSubmit={() => {
          setUpdateModalVisible(false);
          actionRef.current?.reload();
        }}
        onCancel={() => {
          setUpdateModalVisible(false);
        }}
      />
      <UpdateBankModal
        questionId={currentRow?.id}
        visible={updateBankModalVisible}
        onCancel={() => {
          setUpdateBankModalVisible(false);
        }}
      />
    </PageContainer>
  );
};

export default QuestionTableList;
