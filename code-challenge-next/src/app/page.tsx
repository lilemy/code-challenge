import type { Metadata } from "next";
import { Button } from "antd";
import "./index.css";

export const metadata: Metadata = {
  title: "小新知识分享平台",
  description: "小新知识分享平台",
};

export default function HomePage() {
  return (
    <div id="homePage">
      <Button type="primary">按钮</Button>
    </div>
  );
}
