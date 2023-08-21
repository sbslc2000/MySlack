import styled from "styled-components";
import React from "react";
import Navbar from "../component/workspace/navbar/Navbar";
import Sidebar from "../component/workspace/sidebar/Sidebar";
import ChannelSection from "../component/workspace/mainsection/channel/ChannelSection";
import MainSection from "../component/workspace/mainsection/MainSection";
import {useEffect, useRef, useState} from "react";
import axios from "axios";
import {useLocation} from "react-router-dom";
import SidebarFrame from "../component/workspace/sidebar/SidebarFrame";
import CreateChannelModal from "../component/workspace/createchannel/CreateChannelModal";
import InviteWorkspaceModal from "../component/workspace/inviteworkspace/InviteWorkspaceModal";
import MyLogger from "../util/MyLogger";
import ErrorModal from "../component/modal/ErrorModal";
import {getUser} from "../api/user";
import {getChannelsByWorkspaceId} from "../api/channel";
import {getWorkspace} from "../api/workspace";
import AddMemberToChannelModal from "../component/modal/AddMemberToChannelModal";
import {Environment} from "../api/Environment";


const RefreshContext = React.createContext();
const ChannelContext = React.createContext();
const WebSocketSendMessageRequestContext = React.createContext();

const Wrapper = styled.div`
  display:flex;
  flex-direction:column;
  width:100vw;
  height:100vh;
`;

const Section = styled.div`
  display:flex;
  flex:1;
  height:90%;
`;

function WorkspacePage() {

    const location = useLocation()

    const [socketConnected, setSocketConnected] = useState(false);
    const [isWorkspaceLoading, setIsWorkspaceLoading] = useState(true);
    const [isChannelLoading, setIsChannelLoading] = useState(true);
    const [workspace, setWorkspace] = useState({});
    const [currentChannel, setCurrentChannel] = useState({});
    const [channels, setChannels] = useState([]);
    const [directMessageRefresh, setDirectMessageRefresh] = useState(false);
    const [messagesRefresh, setMessagesRefresh] = useState(false);
    const [usersTypingInfo, setUsersTypingInfo] = useState([]);
    const [user, setUser] = useState({});
    const [sendTypingMessage, setSendTypingMessage] = useState(false);
    const [sendTypingEndMessage, setSendTypingEndMessage] = useState(false);

    const messageTypingEndReceiveHandler = (typingInfo) => {
        MyLogger.trace("messageTypingEndReceiveHandler called");
        MyLogger.data("websocket으로부터 받은 typingInfo","typingInfo", typingInfo);

        const parsedData = {
            channelId: typingInfo.channelId,
            user: JSON.parse(typingInfo.user)
        }

        setUsersTypingInfo((prev) => prev.filter((prevTypingInfo) => {
            return !(prevTypingInfo.channelId === parsedData.channelId && prevTypingInfo.user.id === parsedData.user.id);
        }));
    }

    const messageTypingStartReceiveHandler = (typingInfo) => {
        MyLogger.trace("messageTypingStartReceiveHandler called");
        MyLogger.data("websocket으로부터 받은 typingInfo","typingInfo", typingInfo);

        const parsedData = {
            channelId: typingInfo.channelId,
            user: JSON.parse(typingInfo.user)
        }

        setUsersTypingInfo((prev) => [...prev, parsedData]);
        MyLogger.data("typingInfo 가 더해진 usersTypingInfo State", "usersTypingInfo", usersTypingInfo);
    };

    const webSocketSendMessageRequestState = {
        sendTypingMessage: sendTypingMessage,
        setSendTypingMessage: setSendTypingMessage,
        sendTypingEndMessage: sendTypingEndMessage,
        setSendTypingEndMessage: setSendTypingEndMessage

    }

    const refreshState = {
        messagesRefresh: messagesRefresh,
        setMessagesRefresh: setMessagesRefresh,
        directMessageRefresh: directMessageRefresh,
        setDirectMessageRefresh: setDirectMessageRefresh,
        usersTypingInfo: usersTypingInfo,
        setUsersTypingInfo: setUsersTypingInfo
    }


    const channelState = {
        currentChannel: currentChannel,
        setCurrentChannel: setCurrentChannel,
        channels: channels
    };


    const onMessageHandler = (response) => {
        const message = response.message;
        MyLogger.debug("Websocket Message Received");
        MyLogger.debug("Message : "+ message);
        MyLogger.debug("body : "+ JSON.stringify(response.body));
        if (message === "REFRESH_DM_USER_LIST") {
            setDirectMessageRefresh(true);
        } else if (message === "REFRESH_CHANNEL_LIST") {
            getChannelsByWorkspaceId().then(setChannels);
        } else if (message === "REFRESH_MESSAGES") {
            setMessagesRefresh(true);
        } else if (message === "MESSAGE_TYPING_START") {
            messageTypingStartReceiveHandler(response.body);
        } else if (message === "MESSAGE_TYPING_END") {
            messageTypingEndReceiveHandler(response.body);
        }
    }


    const ws = useRef(null);

    useEffect(() => {
        if (!ws.current) {

            const webSocketUrl = Environment.SOCKET_PROTOCOL+"://"+Environment.BACKEND_API_HOST+(Environment.BACKEND_API_PORT ? ":"+Environment.BACKEND_API_PORT : "")+"/ws";
            ws.current = new WebSocket(webSocketUrl);
            ws.current.onopen = () => {
                console.log("connected to " + webSocketUrl);
                setSocketConnected(true);
            };
            ws.current.onclose = (error) => {
                console.log("disconnect from " + webSocketUrl);
                console.log(error);
            };
            ws.current.onerror = (error) => {
                console.log("connection error " + webSocketUrl);
                console.log(error);
            }
            ws.current.onmessage = (response) => {
                const parsedResponse = JSON.parse(response.data);
                onMessageHandler(parsedResponse);
            }
        }
        return () => {
            console.log("clean up");
            ws.current.close();
        };
    }, []);

    /*
    useEffect(() => {
        if (socketConnected) {
            ws.current.send(
                JSON.stringify({
                    message: sendMessage,
                })
            );

            setSendMessage(true);
        }
    }, [socketConnected]);

    // send 후에 onmessage로 데이터 가져오기
    useEffect(() => {
        if (sendMessage) {
            ws.current.onmessage = (evt) => {
                const data = JSON.parse(evt.data);
                console.log(data);
            };
        }
    }, [sendMessage]);
    */

    useEffect(() => {
        if (socketConnected) {
            if (webSocketSendMessageRequestState.sendTypingMessage === true) {

                let body = {
                    workspaceId: workspace.id,
                    channelId: channelState.currentChannel.id,
                    userId: user.id
                }

                ws.current.send(
                    JSON.stringify({
                        message: "MESSAGE_TYPING_START",
                        body: body
                    }));

                MyLogger.debug("send websocket , message : MESSAGE_TYPING_START , body:"+JSON.stringify(body));
                webSocketSendMessageRequestState.setSendTypingMessage(false);
            } else if (webSocketSendMessageRequestState.sendTypingEndMessage === true) {

                let body = {
                    workspaceId: workspace.id,
                    channelId: channelState.currentChannel.id,
                    userId: user.id
                }

                ws.current.send(
                    JSON.stringify({
                        message: "MESSAGE_TYPING_END",
                        body: body
                    }));

                MyLogger.debug("send websocket , message : MESSAGE_TYPING_END , body:"+JSON.stringify(body));
                webSocketSendMessageRequestState.setSendTypingEndMessage(false);
            }
        }

    }, [webSocketSendMessageRequestState])

    //최초 수행
    useEffect(() => {
        //워크스페이스 정보 가져오기
        const workspaceId = location.pathname.split("/")[3];
        getWorkspace(workspaceId).then((workspace) => {
            setWorkspace(workspace);
            setIsWorkspaceLoading(false);
        })


        //채널 정보 가져오기
        getChannelsByWorkspaceId().then((res) => {
           setChannels(res);
           setCurrentChannel(res[0]);
           setIsChannelLoading(false);
        });

        //유저 정보 가져오기
        getUser().then(setUser);
    }, []);


    let content;

    if (isWorkspaceLoading || isChannelLoading) {
        return  <>
            <SidebarFrame></SidebarFrame>
            <MainSection>
                <div style={{color: "white"}}>Loading...</div>
            </MainSection>
        </>
    }

    return (
        <WebSocketSendMessageRequestContext.Provider value={webSocketSendMessageRequestState}>
            <ChannelContext.Provider value={channelState}>
                <RefreshContext.Provider value={refreshState}>
                    <Wrapper>
                        <Navbar/>
                        <Section>
                            <Sidebar channelState={channelState} workspace={workspace}></Sidebar>
                            <ChannelSection channelState={channelState} workspace={workspace}></ChannelSection>
                        </Section>


                        <CreateChannelModal channelState={channelState} workspace={workspace}></CreateChannelModal>
                        <InviteWorkspaceModal workspace={workspace}></InviteWorkspaceModal>
                        <ErrorModal> </ErrorModal>
                        <AddMemberToChannelModal></AddMemberToChannelModal>
                    </Wrapper>
                </RefreshContext.Provider>
            </ChannelContext.Provider>
        </WebSocketSendMessageRequestContext.Provider>
    );
}

export default WorkspacePage;
export {RefreshContext};
export {ChannelContext};
export {WebSocketSendMessageRequestContext};