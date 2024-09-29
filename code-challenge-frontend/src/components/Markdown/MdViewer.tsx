import gfm from '@bytemd/plugin-gfm';
import highlight from '@bytemd/plugin-highlight';
import { Viewer } from '@bytemd/react';
import { setTheme } from 'bytemd-plugin-theme';
import 'bytemd/dist/index.css';
import 'highlight.js/styles/vs.css';
import { useEffect } from 'react';

interface Props {
  value?: string;
  theme?: string;
}

const plugins = [gfm(), highlight()];

/**
 * Markdown 浏览器
 * @param props
 * @constructor
 */
const MdViewer = (props: Props) => {
  const { value = '', theme = 'qklhk-chocolate' } = props;

  useEffect(() => {
    setTheme(theme);
  }, [theme]);

  return (
    <div className="md-viewer">
      <Viewer value={value} plugins={plugins} />
    </div>
  );
};

export default MdViewer;
