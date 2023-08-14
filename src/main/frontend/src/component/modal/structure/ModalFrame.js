import styled from "styled-components";

const ModalDialog = styled.div`
     --bs-modal-width: ${props => props.width}px;
`;
const ModalContent = styled.div`
    background-color: #1A1D21;
    color:white;
    padding-left:20px;
    padding-right:20px;
`;

const ModalFrame = (props) => {

    const id = props.id;
    const width = props.width;

    return (
        <div className="modal fade" id={id} tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <ModalDialog width={width} className="modal-dialog modal-dialog-centered">
                <ModalContent className="modal-content">
                    {props.children}
                </ModalContent>
            </ModalDialog>
        </div>
    );
}

export default ModalFrame;