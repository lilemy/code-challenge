"use client";
import React, { useState } from "react";
import MdEditor from "@/components/MdEditor";
import MdViewer from "@/components/MdViewer";
import "./index.css"

const Banks: React.FC = () => {
  const [text, setText] = useState("");
  return (
    <div id="banks">
      <MdEditor value={text} onChange={setText}></MdEditor>
      <br />
      <MdViewer value={text}></MdViewer>
    </div>
  );
};

export default Banks;
