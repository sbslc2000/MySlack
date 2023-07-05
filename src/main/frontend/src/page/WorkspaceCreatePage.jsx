import styled from "styled-components";
import Navbar from "../component/workspace/navbar/Navbar";
import NavbarFrame from "../component/workspace/navbar/NavbarFrame";
import Sidebar from "../component/workspace/sidebar/Sidebar";
import SidebarFrame from "../component/workspace/sidebar/SidebarFrame";
import MainSection from "../component/workspace/mainsection/MainSection";
import ChannelSection from "../component/workspace/mainsection/channel/ChannelSection";
import WorkspaceNameFormSection from "../component/workspacecreate/WorkspaceNameFormSection";
import WorkspaceChannelFormSection from "../component/workspacecreate/WorkspaceChannelFormSection";
import WorkspaceEmailFormSection from "../component/workspacecreate/WorkspaceEmailFormSection";
import WorkspaceCreatedSection from "../component/workspacecreate/WorkspaceCreatedSection";
import {useState} from "react";
import axios from "axios";
import React from "react";
const Wrapper = styled.div`
    width:1920px;
    height:961px;
`;

const RequestDtoContext = React.createContext();

function WorkspaceCreatePage() {
    const [isLoading, setIsLoading] = useState(false);
    const [progress, setProgress] = useState(1);
    const [request, setRequest] = useState(false);
    const [workspaceCreateRequestDto, setWorkspaceCreateRequestDto] = useState({
        name: "",
        channel: "",
        invitees: []
    });

    const contextValue = {
        workspaceCreateRequestDto: workspaceCreateRequestDto,
        setWorkspaceCreateRequestDto: setWorkspaceCreateRequestDto
    };

    const [createdWorkspaceId, setCreatedWorkspaceId] = useState(null);

    const createWorkspace = () => {

        setIsLoading(true);
        axios.post("/api/workspaces", workspaceCreateRequestDto).then((response) => {
            setCreatedWorkspaceId(response.data.result);
            console.log(createdWorkspaceId);
            setIsLoading(false);
        }).catch((error) => {
            console.log(error);
        });


    }


    const turnNext = () => {
        setProgress(progress + 1)
    }


    console.log(workspaceCreateRequestDto);


    let content;
    if (progress === 1) {
        content = <WorkspaceNameFormSection turnNext={turnNext}></WorkspaceNameFormSection>;
    } else if (progress === 2) {
        content = <WorkspaceChannelFormSection turnNext={turnNext}></WorkspaceChannelFormSection>;
    } else if (progress === 3) {
        content = <WorkspaceEmailFormSection turnNext={turnNext}></WorkspaceEmailFormSection>;
    } else {
        if (!request) {
            createWorkspace();
            setRequest(true);
        }

        content = <WorkspaceCreatedSection workspaceId={createdWorkspaceId}></WorkspaceCreatedSection>;
    }

    if (isLoading) {
        content = <div>로딩중...</div>;
    }

    return (
        <RequestDtoContext.Provider value={contextValue}>
            <Wrapper>
                <NavbarFrame></NavbarFrame>
                <div className="d-flex" style={{width: 1920}}>
                    <SidebarFrame></SidebarFrame>
                    <MainSection>
                        {content}
                    </MainSection>
                </div>
            </Wrapper>
        </RequestDtoContext.Provider>
    );
}

export default WorkspaceCreatePage;
export {RequestDtoContext};
