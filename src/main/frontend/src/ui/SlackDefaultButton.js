import styled from "styled-components";

const Button = styled.button`
    background-color:${props => props.backgroundColor};
    height:${props => props.height}px;
    width:${props => props.width}px;
    border:0;
    color:white;
    display:flex;
    align-items:center;
    justify-content:center;
    border-radius:4px;
    font-weight:900;
    font-size:${props => props.fontSize}px;
    
    &:hover {
        background-color:${props => props.hoverColor};
    }
`;

function SlackDefaultButton(props) {

    const colorType = props.color;
    let backgroundColor;
    let hoverColor;
    if(colorType === "primary"){
        backgroundColor = "#007A5A";
        hoverColor = "#148567";
    } else if(colorType === "back"){
        backgroundColor = "#1A1D21";
        hoverColor = "#222529";
    }

    return (
        <Button width={props.width}
                height={props.height}
                onClick={props.onClick}
                fontSize={props.fontSize}
                backgroundColor = {backgroundColor}
                hoverColor = {hoverColor}
                data-bs-dismiss = {props.dataBsDismiss}
        >
            {props.title}
        </Button>
    );
}

export default SlackDefaultButton;