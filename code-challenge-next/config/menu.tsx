import { MenuDataItem } from "@ant-design/pro-layout";
import {
  BarsOutlined,
  CrownOutlined,
  SmileOutlined,
  TableOutlined,
} from "@ant-design/icons";
import accessEnum from "@/access/accessEnum";

// 菜单列表
export const menus = [
  {
    path: "/",
    name: "主页",
    icon: <SmileOutlined />,
  },
  {
    path: "/banks",
    name: "题库",
    icon: <TableOutlined />,
  },
  {
    path: "/questions",
    name: "题目",
    icon: <BarsOutlined />,
  },
  {
    path: "/admin",
    name: "管理",
    icon: <CrownOutlined />,
    access: accessEnum.ADMIN,
    children: [
      { path: "/admin/user", name: "用户管理", access: accessEnum.ADMIN },
    ],
  },
] as MenuDataItem[];

// 根据路径查找所有菜单
export const findAllMenuItemByPath = (path: string): MenuDataItem | null => {
  return findMenuItemByPath(menus, path);
};

// 根据路径查找菜单
export const findMenuItemByPath = (
  menus: MenuDataItem[],
  path: string,
): MenuDataItem | null => {
  for (const menu of menus) {
    if (menu.path === path) {
      return menu;
    }
    if (menu.children) {
      const matchedMenuItem = findMenuItemByPath(menu.children, path);
      if (matchedMenuItem) {
        return matchedMenuItem;
      }
    }
  }
  return null;
};

// 修改菜单项导出方式为 export
export const menu = [menus, findMenuItemByPath, findAllMenuItemByPath];
