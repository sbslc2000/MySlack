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


const RefreshContext = React.createContext();
const ChannelContext = React.createContext();
const WebSocketSendMessageRequestContext = React.createContext();

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

    const fetchChannels = (cursor) => {

        console.log("fetchChannel called");
        const workspaceId = location.pathname.split("/")[3];
        axios.get("/api/workspaces/" + workspaceId + "/channels").then((response) => {
            if (response.data.isSuccess) {
                //let index = str === "default" ? 0 : response.data.result.length-1;
                setChannels(response.data.result);

                if (cursor === "last") {
                    setCurrentChannel(response.data.result[response.data.result.length - 1]);
                } else if (cursor === "first") {
                    setCurrentChannel(response.data.result[0]);
                }

                setIsChannelLoading(false);
            }
        }).catch((error) => {

        });
    }

    const channelState = {
        currentChannel: currentChannel,
        setCurrentChannel: setCurrentChannel,
        channels: channels,
        fetchChannels: fetchChannels
    };


    const onMessageHandler = (response) => {
        const message = response.message;
        MyLogger.trace("Message : "+ message);
        if (message === "REFRESH_DM_USER_LIST") {
            setDirectMessageRefresh(true);
        } else if (message === "REFRESH_CHANNEL_LIST") {
            fetchChannels();
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

            const webSocketUrl = "ws://localhost:8080/ws";
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
                ws.current.send(
                    JSON.stringify({
                        message: "MESSAGE_TYPING_START",
                        body: {
                            workspaceId: workspace.id,
                            channelId: channelState.currentChannel.id,
                            userId: user.id
                        }
                    }));

                webSocketSendMessageRequestState.setSendTypingMessage(false);
            } else if (webSocketSendMessageRequestState.sendTypingEndMessage === true) {
                ws.current.send(
                    JSON.stringify({
                        message: "MESSAGE_TYPING_END",
                        body: {
                            workspaceId: workspace.id,
                            channelId: channelState.currentChannel.id,
                            userId: user.id
                        }
                    }));

                webSocketSendMessageRequestState.setSendTypingEndMessage(false);
            }
        }

    }, [webSocketSendMessageRequestState])


    const fetchWorkspace = () => {
        const workspaceId = location.pathname.split("/")[3]
        axios.get("/api/workspaces/" + workspaceId).then((response) => {
            if (response.data.isSuccess) {
                setWorkspace(response.data.result);
                setIsWorkspaceLoading(false);
            }
        }).catch((error) => {

        });
    }

    const fetchUser = () => {
        axios.get("/api/users/me").then((response) => {
            if (response.data.isSuccess) {
                setUser(response.data.result);
            }
        }).catch((error) => {

        });
    }


    useEffect(() => {
        fetchWorkspace();
        fetchChannels("first");
        fetchUser();
    }, []);


    let content;

    if (isWorkspaceLoading || isChannelLoading) {
        content = <>
            <SidebarFrame></SidebarFrame>
            <MainSection>
                <div style={{color: "white"}}>Loading...</div>
            </MainSection>
        </>
    } else {
        content = <>
            <Sidebar channelState={channelState} workspace={workspace}></Sidebar>
            <MainSection>
                <ChannelSection channelState={channelState} workspace={workspace}></ChannelSection>
            </MainSection>
        </>;
    }

    return (
        <WebSocketSendMessageRequestContext.Provider value={webSocketSendMessageRequestState}>
            <ChannelContext.Provider value={channelState}>
                <RefreshContext.Provider value={refreshState}>
                    <div>
                        <Navbar/>
                        <div className="d-flex" style={{width: 1920}}>
                            {content}
                        </div>
                        <CreateChannelModal channelState={channelState} workspace={workspace}></CreateChannelModal>
                        <InviteWorkspaceModal workspace={workspace}></InviteWorkspaceModal>
                        <ErrorModal> </ErrorModal>
                    </div>
                </RefreshContext.Provider>
            </ChannelContext.Provider>
        </WebSocketSendMessageRequestContext.Provider>
    );
}

export default WorkspacePage;
export {RefreshContext};
export {ChannelContext};
export {WebSocketSendMessageRequestContext};