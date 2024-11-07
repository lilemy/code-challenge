import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import accessEnum from "@/access/accessEnum";

// 默认用户
const DEFAULT_USER: API.LoginUserVO = {
  username: "未登录",
  userProfile: "暂无简介",
  userAvatar: "/user.png",
  userRole: accessEnum.NOT_LOGIN,
};

/**
 * 登录用户全局状态
 */
export const loginUserSlice = createSlice({
  name: "loginUser",
  initialState: DEFAULT_USER,
  reducers: {
    setLoginUser: (state, action: PayloadAction<API.LoginUserVO>) => {
      return {
        ...action.payload,
      };
    },
  },
});

// 修改状态
export const { setLoginUser } = loginUserSlice.actions;

export default loginUserSlice.reducer;
