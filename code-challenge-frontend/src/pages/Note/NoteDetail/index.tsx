import NoteCard from '@/components/NoteCard';
import React, {useEffect, useState} from 'react';
import {useParams} from "@@/exports";
import {getNoteVoById} from "@/services/code-challenge/noteController";
import {message} from "antd";

const NoteDetailPage: React.FC = () => {
  const params = useParams();
  const { noteId } = params;
  const [note, setNote] = useState<API.NoteVO>({});

  // 获取数据
  const loadData = async () => {
    if (!noteId) {
      message.error('笔记不存在');
      return;
    }
    try {
      const res = await getNoteVoById({
        id: noteId as any,
      });
      setNote(res.data ?? {});
    } catch (e: any) {
      message.error('获取笔记详情失败，' + e.message);
    }
  };

  useEffect(() => {
    loadData().then(() => '');
  }, []);

  return (
    <div className="max-width-content">
      <NoteCard note={note} />
    </div>
  );
};

export default NoteDetailPage;
