import { addUserSignIn } from '@/services/code-challenge/userController';
import { message } from 'antd';
import { useEffect, useState } from 'react';

/**
 * 添加用户刷题签到记录钩子
 * @constructor
 */
const useAddUserSignInRecord = () => {
  // 签到状态
  const [loading, setLoading] = useState<boolean>(true);

  // 请求后端执行签到
  const doFetch = async () => {
    setLoading(true);
    try {
      await addUserSignIn({});
    } catch (e: any) {
      message.error('获取刷题签到记录失败，' + e.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    doFetch();
  }, []);

  return { loading };
};

export default useAddUserSignInRecord;
