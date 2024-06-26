

export default function addTokenHeader(handler) {
    return async (req, res) => {
        // Извлекаем токен из cookie
        const token = req.cookies.token;

        // Добавляем токен в заголовок запроса
        if (token) {
            req.headers.authorization = `Bearer ${token}`;
        }

        // Вызываем следующий middleware или обработчик маршрута
        return handler(req, res);
    };
}