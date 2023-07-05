import styled from "styled-components";

const MenuItem = styled.button`
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

export default MenuItem;