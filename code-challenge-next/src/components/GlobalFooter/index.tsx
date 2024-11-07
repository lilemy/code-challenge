import React from "react";
import "./index.css";
import { Space } from "antd";

export default function GlobalFooter() {
  const currentYear = new Date().getFullYear();
  return (
    <div className="globalFooter">
      <Space>
        <a href="https://github.com/lilemy/code-challenge" target="_blank">
          源码 Github 仓库地址
        </a>
      </Space>
      <br />
      <Space>
        <div>© {currentYear} 小新知识分享平台 |</div>
        <a href="https://beian.miit.gov.cn/" target="_blank">
          渝ICP备2024030252号-1
        </a>
      </Space>
    </div>
  );
}
