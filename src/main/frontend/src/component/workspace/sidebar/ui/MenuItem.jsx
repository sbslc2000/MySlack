import styled from "styled-components";

const Wrapper = styled.button`
    width:260px;
    display:flex;
    background-color:${props => props.selected ?  "#1164A3" : "inherit"};
    color:${props => props.selected ?  "#F3F7FA" : "inherit"};
    border:0;
    text-align:left;
    font-size:15px;
    height:28px;
    padding: 0 16px 0 20px;
    font-weight:700;
    margin: 2px 0px 0px 0px;
    
    &: hover {
        background-color:${props => props.selected ? "#1164A3": "#27242C" };
    }
    line-height:28px;
`;

const Tag = styled.span`
    width: 17px;
    display: inline-block;
`;

function MenuItem(props) {

    const dataBsToggle = props.dataBsToggle;
    const dataBsTarget = props.dataBsTarget;
    const onClickHandler = props.onClick;

    const tag = props.tag;
    return (
        <Wrapper
            selected={props.selected}
            id = {props.id}
            data-bs-toggle={dataBsToggle}
            data-bs-target={dataBsTarget}
            onClick={onClickHandler}
        >
            {tag && <Tag>{tag}</Tag>}
            {props.children}
        </Wrapper>
    )
}

export default MenuItem;