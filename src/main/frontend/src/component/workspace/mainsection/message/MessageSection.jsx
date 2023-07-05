import styled from 'styled-components';
import SeparationLine from "./SeparationLine";
import DefaultUserIcon from "../../../../img/default_user.png";
import MessageWithName from "./MessageWithName";
import {useLocation} from "react-router-dom";
import axios from "axios";
import {useContext, useEffect, useState} from "react";
import MessageWithoutName from "./MessageWithoutName";
import {RefreshContext} from "../../../../page/WorkspacePage";

const Wrapper = styled.div`
    min-height:495px;
`;

const MessageWithNameWrapper = styled.div`
    padding-left:20px;
    padding-right:20px;
    display:flex;
    &:hover{
        background-color:#222529;
    }
    padding-top:16px;
`;

const MessageWithoutNameWrapper = styled.div`
    padding-top:5px;
    padding-left:66px;
    padding-right:20px;
    display:flex;
    &:hover{
        background-color:#222529;
    }
`;

const MessageUserIcon = styled.img`
    width: 36px;
    height: 36px;
    border-radius:5px;
`;

const MessageBodyWithName = styled.div`
    width: 1584px;
    display:flex;
    flex-direction:column;
    padding-left:10px;
    vertical-align:middle;
    
     &:hover{
        background-color:#222529;
    }
`;

const MessageBody = styled.div`
    font-size:16px;
    display:inline-block;
    > span {
         white-space: pre-wrap;
          word-break: break-all;
    }
`;


function MessageSection(props) {
    const channel = props.channel;
    let location = useLocation();
    let workspaceId = location.pathname.split("/")[3];
    const [messages, setMessages] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    let refreshState = useContext(RefreshContext);

    const fetchMessage = () => {
        const url = "/api/workspaces/" + workspaceId + "/channels/" + channel.id + "/messages";
        axios.get(url).then((response) => {
            if (response.data.isSuccess) {
                const messages = response.data.data;
                console.log(messages);

                setMessages(response.data.result);
                setIsLoading(false);
            }
        }).catch((error) => {

        });
    }

    useEffect(() => {
        fetchMessage();
    }, []);

    if(refreshState.messagesRefresh) {
        console.log("messageRefresh");
        setTimeout(()=> {
            fetchMessage();
        },100);
        refreshState.setMessagesRefresh(false);
    }


    const naiveContent =
        <>
            <MessageWithNameWrapper>
                <MessageUserIcon src={DefaultUserIcon}>
                </MessageUserIcon>
                <MessageBodyWithName>
                    <div style={{height: 22, fontSize: 16, display: "inline-block"}}>
                        <span style={{fontWeight: 900}}>sbslc2000</span>
                        <span style={{paddingLeft: 5, fontSize: 14}}>오후 3:24</span>
                    </div>
                    <MessageBody>
                        <span>MessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHIMessageMessage HIHI</span>
                    </MessageBody>
                </MessageBodyWithName>
            </MessageWithNameWrapper>
            <MessageWithNameWrapper>
                <MessageUserIcon src={DefaultUserIcon}>
                </MessageUserIcon>
                <MessageBodyWithName>
                    <div style={{height: 22, fontSize: 16, display: "inline-block"}}>
                        <span style={{fontWeight: 900}}>sbslc2000</span>
                        <span style={{paddingLeft: 5, fontSize: 14}}>오후 3:24</span>
                    </div>
                    <MessageBody>
                        <span>MessageMessage</span>
                    </MessageBody>
                </MessageBodyWithName>
            </MessageWithNameWrapper>
            <MessageWithoutNameWrapper>
                <MessageBody>Message</MessageBody>
            </MessageWithoutNameWrapper>
            <MessageWithoutNameWrapper>
                <MessageBody>Message</MessageBody>
            </MessageWithoutNameWrapper>
            <MessageWithoutNameWrapper>
                <MessageBody>
                    <span>
                MessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessageMessage
                    </span>
                </MessageBody>
            </MessageWithoutNameWrapper>
        </>;

    let prevSender = {};

    let content = <>
        <SeparationLine date={"6월 27일 화요일"}></SeparationLine>
        {messages.map((message) => {
            if(prevSender.id !== message.sender.id){

                prevSender = message.sender;
                return <MessageWithName message={message}/>
            } else {
                prevSender = message.sender;
                return <MessageWithoutName message={message}/>
            }
        })}
    </>;

    if(isLoading){
        content = <></>;
    }

    return (
        <Wrapper>
            {content}
        </Wrapper>
    );
}

export default MessageSection;