"use client";
import {
  GithubFilled,
  LogoutOutlined,
  SearchOutlined,
} from "@ant-design/icons";
import { ProLayout } from "@ant-design/pro-components";
import { Dropdown, Input, message } from "antd";
import React from "react";
import Image from "next/image";
import { usePathname, useRouter } from "next/navigation";
import Link from "next/link";
import GlobalFooter from "@/components/GlobalFooter";
import "./index.css";
import { menus } from "../../../config/menu";
import getAccessibleMenus from "@/access/menuAccess";
import { userLogout } from "@/api/userController";
import { setLoginUser } from "@/stores/loginUser";
import { DEFAULT_USER } from "@/constants/user";
import { useDispatch, useSelector } from "react-redux";
import { AppDispatch, RootState } from "@/stores";

const SearchInput = () => {
  return (
    <div
      key="SearchOutlined"
      aria-hidden
      style={{
        display: "flex",
        alignItems: "center",
        marginInlineEnd: 24,
      }}
      onMouseDown={(e) => {
        e.stopPropagation();
        e.preventDefault();
      }}
    >
      <Input
        style={{
          borderRadius: 4,
          marginInlineEnd: 12,
        }}
        prefix={<SearchOutlined />}
        placeholder="搜索"
        variant="borderless"
      />
    </div>
  );
};

interface Props {
  children: React.ReactNode;
}

export default function BasicLayout({ children }: Props) {
  const pathname = usePathname();
  const router = useRouter();
  const dispatch = useDispatch<AppDispatch>();
  // 获得登录用户信息
  const loginUser = useSelector((state: RootState) => state.loginUser);
  /**
   * 用户注销
   */
  const doUserLogout = async () => {
    try {
      await userLogout();
      message.success("已退出登录");
      dispatch(setLoginUser(DEFAULT_USER));
      router.push("/user/login");
    } catch (e: any) {
      message.error("操作失败，" + e.message);
    }
    return;
  };

  return (
    <div
      id="basicLayout"
      style={{
        height: "100vh",
        overflow: "auto",
      }}
    >
      <ProLayout
        layout="top"
        title="小新知识分享平台"
        location={{
          pathname,
        }}
        logo={
          <Image
            src="/logo.svg"
            height={32}
            width={32}
            alt="小新知识分享平台"
          />
        }
        avatarProps={{
          src: loginUser.userAvatar || "/user.png",
          size: "small",
          title: loginUser.username || "未命名",
          render: (_, dom) => {
            return loginUser.id ? (
              <Dropdown
                menu={{
                  items: [
                    {
                      key: "logout",
                      icon: <LogoutOutlined />,
                      label: "退出登录",
                    },
                  ],
                  onClick: async (event: { key: React.Key }) => {
                    const { key } = event;
                    // 退出登录
                    if (key === "logout") {
                      await doUserLogout();
                    }
                  },
                }}
              >
                {dom}
              </Dropdown>
            ) : (
              <div onClick={() => router.push("/user/login")}>{dom}</div>
            );
          },
        }}
        actionsRender={(props) => {
          if (props.isMobile) return [];
          return [
            <SearchInput key="search" />,
            <a key="github" href="https://github.com/lilemy" target="_blank">
              <GithubFilled key="GithubFilled" />
            </a>,
          ];
        }}
        footerRender={() => <GlobalFooter />}
        onMenuHeaderClick={() => router.push("/")}
        menuDataRender={() => {
          return getAccessibleMenus(loginUser, menus);
        }}
        // 定义菜单项渲染方式
        menuItemRender={(item, dom) => (
          <Link href={item.path || "/"} target={item.target}>
            {dom}
          </Link>
        )}
      >
        {children}
      </ProLayout>
    </div>
  );
}
