"use client";
import React from "react";
import { Card, message } from "antd";
import { useRouter } from "next/navigation";
import { useDispatch } from "react-redux";
import { AppDispatch } from "@/stores";
import { setLoginUser } from "@/stores/loginUser";
import { LoginForm, ProForm, ProFormText } from "@ant-design/pro-form";
import { LockOutlined, UserOutlined } from "@ant-design/icons";
import Image from "next/image";
import Link from "next/link";
import { userLogin } from "@/api/userController";
import LoginUserVO = API.LoginUserVO;

/**
 * 用户登录页面
 * @constructor
 */
const UserLoginPage: React.FC = () => {
  const [form] = ProForm.useForm();
  const router = useRouter();
  const dispatch = useDispatch<AppDispatch>();
  /**
   * 提交登录
   * @param values
   */
  const handleSubmit = async (values: API.UserLoginRequest) => {
    try {
      // 登录
      const res = await userLogin(values);
      if (res.data) {
        message.success("登录成功！");
        // 保存已登录用户信息
        dispatch(setLoginUser(res.data as LoginUserVO));
        router.replace("/");
        form.resetFields();
      }
      // 如果失败去设置用户错误信息
    } catch (error: any) {
      message.error("登录失败，" + error.message);
    }
  };
  return (
    <div
      id="userLoginPage"
      style={{
        flex: "1",
        padding: "32px 0",
      }}
    >
      <Card
        style={{
          width: "460px",
          margin: "auto",
          backgroundColor: "rgba(255, 255, 255, 0.7)",
          marginTop: "40px",
        }}
      >
        <LoginForm<API.UserLoginRequest>
          contentStyle={{
            minWidth: 280,
            maxWidth: "75vw",
          }}
          logo={<Image alt="logo" src="/logo.svg" width={44} height={44} />}
          title="小新知识分享平台"
          subTitle={"小新知识分享平台"}
          onFinish={handleSubmit}
        >
          <ProFormText
            name="userAccount"
            fieldProps={{
              size: "large",
              prefix: <UserOutlined />,
            }}
            placeholder={"账号: "}
            rules={[
              {
                required: true,
                message: "账号是必填项！",
              },
            ]}
          />
          <ProFormText.Password
            name="userPassword"
            fieldProps={{
              size: "large",
              prefix: <LockOutlined />,
            }}
            placeholder={"密码:"}
            rules={[
              {
                required: true,
                message: "密码是必填项！",
              },
            ]}
          />

          <div
            style={{
              marginBottom: 24,
            }}
          >
            <div
              style={{
                marginBottom: 24,
                textAlign: "right",
              }}
            >
              <Link href={"/user/register"} prefetch={false}>
                新用户注册
              </Link>
            </div>
          </div>
        </LoginForm>
      </Card>
    </div>
  );
};

export default UserLoginPage;
