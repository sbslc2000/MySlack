import React from 'react';
import './App.css';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import DevPage from "./page/DevPage";
import HomePage from "./page/HomePage";
import WorkspacePage from "./page/WorkspacePage";
import WorkspaceCreatePage from "./page/WorkspaceCreatePage";
import WorkspaceFrame from "./component/workspace/WorkspaceFrame";
import InvitePage from "./page/InvitePage";
import TestPage from "./page/TestPage";

function App() {
  return (
      <BrowserRouter>
          <Routes>
              <Route path="/" element={<HomePage/>}/>
              <Route path="/dev" element={<DevPage/>}/>
              <Route path="/app/workspace/:id" element={<WorkspacePage/>}/>
              <Route path="/workspace/create" element={<WorkspaceCreatePage/>}/>
              <Route path="/workspace/enter/:code" element={<InvitePage/>}/>
              <Route path="/test" element={<TestPage/>}/>
          </Routes>
      </BrowserRouter>
  );
}

export default App;
