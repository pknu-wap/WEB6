// 웹 페이지 이동
document.querySelector('.web-title').onclick = () => {
    window.location.href = '../main_page/index.html';
};

document.querySelector('.login-button').onclick = () => {
    window.location.href = '../login/login-page.html';
};

document.querySelector('.signup-button').onclick = () => {
    window.location.href = '../join_page/join_page2.html';
};

// 검색 결과 및 페이지네이션 설정
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const query = urlParams.get('query') || '';
    const option = urlParams.get('option') || 'title';
    const headingElement = document.getElementById('search-result-heading');
    const noResultsMessage = document.getElementById('no-result-message');
    const dummyImageUrl = "https://via.placeholder.com/200x300"; // 더미 이미지 URL

    if (query) {
        headingElement.innerHTML = `<span>"${query}"의 검색 결과</span>`;
        performSearch(option, query);
    } else {
        noResultsMessage.innerHTML = `<span>검색 결과가 없습니다. 다시 입력해주세요.</span>`;
    }

    document.getElementById("search-form").addEventListener("submit", async (event) => {
        event.preventDefault();
        const query = document.getElementById("search-input").value;
        const option = document.getElementById("search-option").value;

        if (!query) {
            alert("검색어를 입력하세요.");
            return;
        } else {
            headingElement.innerHTML = `<span>"${query}"의 검색 결과</span>`;
            performSearch(option, query);
        }
    });

    async function performSearch(option, query) {
        try {
            if (['nation', 'genre', 'actor', 'director'].includes(option)) {
                const data = await searchByTag(option, query);
                console.log('Data fetched by tag:', data);
                displayResults(data, 1);
                setupPagination(data);
            } else {
                const response = await searchMovies(option, query);
                const data = await response.json();

                if (response.ok && data.success && data.data.length > 0) {
                    displayResults(data.data, 1);
                    setupPagination(data.data);
                } else {
                    noResultsMessage.innerHTML = `<span>검색 결과가 없습니다.</span>`;
                }
            }
        } catch (error) {
            console.error("Error fetching movie data:", error);
            noResultsMessage.innerHTML = `<span>검색 도중 오류가 발생했습니다.</span>`;
        }
    }

    async function searchByTag(option, query) {
        const response = await fetch(
            `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/search/json?option=${option}&query=${encodeURIComponent(query)}`,
            {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            }
        );
        const data = await response.json();
        return Array.isArray(data) ? data : [data]; // 데이터를 배열로 반환
    }
});

async function searchMovies(option, query) {
    const response = await fetch(
        `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/search/json?option=${option}&query=${encodeURIComponent(query)}`,
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        }
    );
    return response;
}

function displayResults(data, page) {
    const resultsContainer = document.getElementById("results-container");
    resultsContainer.innerHTML = "";

    const startIndex = (page - 1) * 12;
    const endIndex = startIndex + 12;
    const pageData = data.slice(startIndex, endIndex);

    const dummyImageUrl = "https://via.placeholder.com/200x300"; // 더미 이미지 URL

    pageData.forEach((item) => {
        const itemElement = document.createElement("div");
        itemElement.classList.add("result-item");

        const posterUrl = (item.postersList && item.postersList.length > 0) ? item.postersList[0] : dummyImageUrl;

        itemElement.innerHTML = `
          <img src="${posterUrl}" alt="${item.title} 포스터">
          <h3 class="result-title">${item.title}</h3>
          <p class="result-details">
            <span class="result-genre">${item.genre}</span>
            <span class="result-nation">· ${item.nation}</span>
          </p>
        `;
        itemElement.addEventListener("click", () => {
            window.location.href = `/detail_page/detail_page.html?id=${item.movieId}`;
        });
        resultsContainer.appendChild(itemElement);
    });
}

function setupPagination(data) {
    const pagination = document.getElementById("pagination");
    const totalPages = Math.ceil(data.length / 12);

    pagination.innerHTML = "";

    for (let i = 1; i <= totalPages; i++) {
        const button = document.createElement("button");
        button.textContent = i;
        button.addEventListener("click", () => {
            displayResults(data, i);
        });
        pagination.appendChild(button);
    }
}
