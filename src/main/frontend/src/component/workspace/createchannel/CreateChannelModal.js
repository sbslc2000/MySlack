import styled from "styled-components";
import SlackDefaultButton from "../../../ui/SlackDefaultButton";
import {useState} from "react";
import axios from "axios";


const ModalDialog = styled.div`
    
`;
const ModalContent = styled.div`
    background-color: #1A1D21;
    color:white;
   
`;

const MessageTextarea = styled.textarea`
    background-color:inherit;
    color:inherit;
    border:1px solid #515357;
    height:44px;
    font-size:18px;
    
    resize:none;
    
    &::placeholder{
        color:#7A7B7D;
        font-weight:900;
    }
    &:focus {
        background-color: #222529;
        color:white;
        border:0;
        box-shadow:none;
    }
`;



function CreateChannelModal(props) {
    const [progress,setProgress] = useState(0);
    const [isPrivate, setIsPrivate] = useState(false);
    const [channelName, setChannelName] = useState("");

    const createChannelHandler = () => {
        let body = {};
        body.name = channelName;
        body.isPrivate = isPrivate;
        body.description = "";
        body.workspaceId = props.workspace.id;

        axios.post("/api/channels",body).then((response) => {
            if(response.data.isSuccess){
                setProgress(0);
                setChannelName("");
                setIsPrivate(false);

                props.channelState.fetchChannels("last");
                //props.channelState.setCurrentChannel(response.data.result);
            }
        }).catch((error) => {

        });
        closeHandler();
    }

    const closeHandler = () => {
        setProgress(0);
        setChannelName("");
        setIsPrivate(false);
    }

    const setPrivateHandler = () => {
        setIsPrivate(true);
    }

    const setPublicHandler = () => {
        setIsPrivate(false);
    }

    const channelNameChangeHandler = (e) => {
      setChannelName(e.target.value);
    };

    const nextButtonHandler = () => {
        setProgress(1);
    }

    const backButtonHandler = () => {
        setProgress(0);
    }

    let modalHeader = progress === 0 ?
        (
            <div style={{borderBottom:0}} className="modal-header">
                <h1 className="modal-title fs-5" id="exampleModalLabel">Create a channel</h1>
                <button onClick={closeHandler} type="button" className="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
    ) : (
        <>
            <div style={{borderBottom:0}} className="modal-header">
                <h1 style={{fontSize:18}} className="modal-title fs-5" id="exampleModalLabel">Create a channel</h1>
                <button onClick={closeHandler} type="button" className="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>

            </div>
            <div style={{paddingLeft:20,fontSize:15,color:"#A6A7A9",position:"relative",top:-10}}># {channelName}</div>
             </>
        );

    let modalBody = progress === 0 ? (
        <div className="modal-body" style={{borderBottom:0}}>
            <div style={{paddingLeft:5,paddingBottom:10}}>
                이름
            </div>
            <form>
                <div className="form-group">
                    <MessageTextarea onChange={channelNameChangeHandler} value={channelName} placeholder="예: 플랜 예산" className="form-control" id="textareaContent"
                                     rows="1"></MessageTextarea>
                </div>
            </form>
            <div style={{color:"#A9A9AB",fontSize:13,paddingLeft:3,paddingRight:3,paddingTop:2}}>
                채널에서는 특정 주제에 대한 대화가 이루어집니다. 찾고 이해하기 쉬운 이름을 사용하세요.
            </div>
        </div>
    ): (
        <div className="modal-body" style={{borderBottom:0}}>
            <div style={{paddingLeft:5,paddingBottom:10}}>
                가시성
            </div>
            <div className="form-check">
                <input onClick={setPublicHandler} className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" checked={isPrivate ? false : true} />
                    <label className="form-check-label" htmlFor="flexRadioDefault1" >
                        공개 - {props.workspace.name}의 누구나
                    </label>
            </div>
            <div className="form-check">
                <input onClick={setPrivateHandler} className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" checked={isPrivate ? true : false} />
                    <label className="form-check-label" htmlFor="flexRadioDefault2">
                        비공개 - 일부 사람만
                    </label>
            </div>
            <div style={{color:"#A9A9AB",fontSize:13,paddingLeft:3,paddingRight:3,paddingTop:2}}>
            </div>
        </div>
    );

    let modalFooter = progress === 0 ? (
        <div className="modal-footer" style={{borderTop:0}}>
            <SlackDefaultButton fontSize={15} width={80} height={36} title={"다음"} color={"primary"} onClick={nextButtonHandler} type="button" data-bs-toggle="modal"  className="btn btn-primary"></SlackDefaultButton>
        </div>
    ): (
        <div className="modal-footer" style={{borderTop:0}}>
            <SlackDefaultButton fontSize={15} width={80} height={36} title={"뒤로"} color={"back"} onClick={backButtonHandler} type="button"  className="btn btn-primary"></SlackDefaultButton>
            <SlackDefaultButton fontSize={15} width={80} height={36} title={"생성"} color={"primary"} onClick={createChannelHandler} type="button" dataBsDismiss={"modal"} className="btn btn-primary"></SlackDefaultButton>
        </div>
    );
    return (
        <div  className="modal fade" id="createChannel" tabIndex="-1" aria-labelledby="exampleModalLabel"
             aria-hidden="true">
            <ModalDialog className="modal-dialog modal-dialog-centered">
                <ModalContent className="modal-content">
                    {modalHeader}
                    <div style={{color:"#C7C8C9"}}>
                        {modalBody}
                    </div>

                    {modalFooter}
                </ModalContent>
            </ModalDialog>
        </div>
    );
}

export default CreateChannelModal;