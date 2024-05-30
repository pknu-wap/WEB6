// 클릭 이벤트 핸들러 등록
var elements = document.getElementsByClassName('web-title');

// 타이틀 클릭 시 메인 페이지로 이동
elements[0].onclick = function () {
    window.location.href = 'https://www.google.com';
};

document.getElementById("submitComment").addEventListener("click", function () {
    const commentText = document.getElementById("commentInput").value;
    if (commentText.trim() !== "") {
        const newCommentDiv = document.createElement("div");
        newCommentDiv.classList.add("comment");
        newCommentDiv.textContent = commentText;
        document.getElementById("comments").appendChild(newCommentDiv);
        document.getElementById("commentInput").value = ""; // 댓글 입력란 초기화
    } else {
        alert("댓글을 작성해주세요.");
    }
});
