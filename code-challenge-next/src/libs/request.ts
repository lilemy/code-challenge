import axios from "axios";
import { BACKEND_HOST_DEV, BACKEND_HOST_PROD } from "@/constants/baseUrl";

const isDev = process.env.NODE_ENV === "development";

// 创建 Axios 示例
const myAxios = axios.create({
  baseURL: isDev ? BACKEND_HOST_DEV : BACKEND_HOST_PROD,
  timeout: 10000,
  withCredentials: true,
});

// 创建请求拦截器
myAxios.interceptors.request.use(
  function (config) {
    // 请求执行前执行
    return config;
  },
  function (error) {
    // 处理请求错误
    return Promise.reject(error);
  },
);

// 创建响应拦截器
myAxios.interceptors.response.use(
  // 2xx 响应触发
  function (response) {
    // 处理响应数据
    const { data } = response as unknown as BaseResponse<any>;
    if (!data) {
      throw new Error("服务异常");
    }
    // 未登录
    if (data.code === 40100) {
      // 不是获取用户信息接口，或者不是登录页面，则跳转到登录页面
      if (
        !response.request.responseURL.includes("user/get/login") &&
        !response.request.responseURL.includes("user/add/sign_in") &&
        !window.location.pathname.includes("/user/login")
      ) {
        window.location.href = `/user/login?redirect=${window.location.href}`;
        throw new Error("请先登录");
      }
    } else if (data.code !== 0) {
      // 其他错误
      throw new Error(data.message ?? "服务器错误");
    }
    return data;
  },
  // 非 2xx 响应触发
  function (error) {
    // 处理响应错误
    return Promise.reject(error);
  },
);

export default myAxios;
