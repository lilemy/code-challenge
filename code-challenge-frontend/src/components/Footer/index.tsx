import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  const defaultMessage = 'lilemy';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      style={{
        background: 'none',
      }}
      links={[
        {
          key: 'code-challenge',
          title: '小新面试刷题平台',
          href: 'https://github.com/lilemy/code-challenge',
          blankTarget: true,
        },
        {
          key: 'github',
          title: (
            <>
              <GithubOutlined /> lilemy
            </>
          ),
          href: 'https://github.com/lilemy',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;
