import { getLoginUser, userEdit } from '@/services/code-challenge/userController';
import { UploadOutlined } from '@ant-design/icons';
import { ProFormText, ProFormTextArea } from '@ant-design/pro-components';
import { ProForm, ProFormInstance } from '@ant-design/pro-form/lib';
import { Button, message, Upload } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import useStyles from './index.style';

const BaseView: React.FC = () => {
  const { styles } = useStyles();
  const formRef = useRef<ProFormInstance>();
  const [loginUser, setLoginUser] = useState<API.LoginUserVO>();
  const [userMessageChange, setUserMessageChange] = useState<boolean>(false);
  // 头像组件 方便以后独立，增加裁剪之类的功能
  const AvatarView = ({ avatar }: { avatar: string }) => (
    <>
      <div className={styles.avatar_title}>头像</div>
      <div className={styles.avatar}>
        <img src={avatar} alt="avatar" />
      </div>
      <Upload showUploadList={false}>
        <div className={styles.button_view}>
          <Button disabled={true}>
            <UploadOutlined />
            更换头像
          </Button>
        </div>
      </Upload>
    </>
  );
  const handleFinish = async (value: API.LoginUserVO) => {
    try {
      console.log(value);
      const res = await userEdit(value);
      message.success('更新信息成功');
      if (res.data) {
        setUserMessageChange(!userMessageChange);
      }
    } catch (e: any) {
      message.error('更新信息失败', e.message);
    }
  };
  const getLoginUserMes = async () => {
    try {
      const res = await getLoginUser();
      setLoginUser(res.data);
    } catch (e: any) {
      message.error('获取用户信息错误，' + e.message);
    }
  };
  useEffect(() => {
    getLoginUserMes().then(() => '');
  }, [userMessageChange]);
  useEffect(() => {
    formRef.current?.resetFields();
  }, [loginUser]);
  return (
    <div className={styles.baseView}>
      <div className={styles.left}>
        <ProForm
          formRef={formRef}
          layout="vertical"
          onFinish={async (value) => {
            await handleFinish({
              id: loginUser?.id,
              ...value,
            });
          }}
          submitter={{
            searchConfig: {
              submitText: '更新基本信息',
            },
          }}
          initialValues={{ ...loginUser }}
        >
          <ProFormText width="lg" name="username" label="昵称" />
          <ProFormTextArea name="userProfile" label="个人简介" placeholder="个人简介" />
        </ProForm>
      </div>
      <div className={styles.right}>
        <AvatarView avatar={loginUser?.userAvatar || '/user.png'} />
      </div>
    </div>
  );
};
export default BaseView;
