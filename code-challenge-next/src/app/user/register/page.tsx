"use client";
import React from "react";
import { LoginForm, ProFormText } from "@ant-design/pro-form";
import { useRouter } from "next/navigation";
import { Card, message } from "antd";
import Image from "next/image";
import { LockOutlined, UserOutlined } from "@ant-design/icons";
import Link from "next/link";
import { userRegister } from "@/api/userController";

const UserRegisterPage: React.FC = () => {
  const router = useRouter();
  /**
   * 提交注册
   * @param values
   */
  const handleSubmit = async (values: API.UserRegisterRequest) => {
    try {
      // 登录
      const res = await userRegister(values);
      if (res.data) {
        message.success("注册成功！请登录");
        // 前往登录页
        router.replace("/user/login");
      }
      // 如果失败去设置用户错误信息
    } catch (error: any) {
      message.error("注册失败，" + error.message);
    }
  };
  return (
    <div id="userRegisterPage">
      <Card
        style={{
          width: "460px",
          margin: "auto",
          backgroundColor: "rgba(255, 255, 255, 0.7)",
          marginTop: "40px",
        }}
      >
        <LoginForm<API.UserRegisterRequest>
          contentStyle={{
            minWidth: 280,
            maxWidth: "75vw",
          }}
          logo={<Image alt="logo" src="/logo.svg" width={44} height={44} />}
          title="小新知识分享平台"
          subTitle={"小新知识分享平台"}
          onFinish={handleSubmit}
          submitter={{
            searchConfig: { submitText: "注册" },
          }}
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
                message: "请输入账号！",
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
                message: "请输入密码！",
              },
            ]}
          />
          <ProFormText.Password
            name="checkPassword"
            fieldProps={{
              size: "large",
              prefix: <LockOutlined />,
            }}
            placeholder={"确认密码:"}
            rules={[
              {
                required: true,
                message: "请再次输入密码！",
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
              已有账号？
              <Link href={"/user/login"} prefetch={false}>
                去登录
              </Link>
            </div>
          </div>
        </LoginForm>
      </Card>
    </div>
  );
};

export default UserRegisterPage;
