import MdEditor from '@/components/Markdown/MdEditor';
import MdViewer from '@/components/Markdown/MdViewer';
import { PageContainer } from '@ant-design/pro-components';
import React, { useState } from 'react';

const Welcome: React.FC = () => {
  const [text, setText] = useState<string>('');
  return (
    <PageContainer className="max-width-content">
      <MdEditor value={text} onChange={setText} />
      <MdViewer value={text} />
    </PageContainer>
  );
};

export default Welcome;
