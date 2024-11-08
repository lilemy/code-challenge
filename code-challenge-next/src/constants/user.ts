import AccessEnum from "@/access/accessEnum";

export const DEFAULT_USER: API.LoginUserVO = {
  username: "未登录",
  userProfile: "暂无简介",
  userAvatar: "/user.png",
  userRole: AccessEnum.NOT_LOGIN,
};
