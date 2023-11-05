import React from 'react';
import { Link } from 'react-router-dom';

const ContextMenu = ({ visible, top, left, username, onClose }) => {
    return visible ? (
        <div
            className="context-menu"
            style={{ top: top + 25, left }}
        >
            <ul>
                <li>
                    <Link to={`/profile/${username}`}>Перейти на страницу профиля</Link>
                </li>
                <li onClick={onClose}>Закрыть</li>
            </ul>
        </div>
    ) : null;
};

export default ContextMenu;
