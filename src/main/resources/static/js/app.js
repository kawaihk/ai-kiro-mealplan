import { API_BASE } from './constants.js';
import { get, post, put, del } from './utils/apiClient.js';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('recipeForm');
    const recipeTableBody = document.getElementById('recipeTableBody');
    const searchBtn = document.getElementById('searchBtn');
    const cancelBtn = document.getElementById('cancelBtn');

    // 初回読み込み
    loadRecipes();

    // 登録・更新イベント
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        clearErrors();

        const id = document.getElementById('recipeId').value;
        const recipeData = {
            title: document.getElementById('title').value,
            description: document.getElementById('description').value,
            calories: parseInt(document.getElementById('calories').value) || 0
        };

        try {
            if (id) {
                await put(`${API_BASE}/${id}`, recipeData);
            } else {
                await post(API_BASE, recipeData);
            }
            resetForm();
            loadRecipes();
        } catch (error) {
            if (error.data) {
                handleApiError(error.data);
            } else {
                alert('通信エラーが発生しました');
            }
        }
    });

    // 検索イベント
    searchBtn.addEventListener('click', async () => {
        const keyword = document.getElementById('searchKeyword').value;
        if (!keyword) return loadRecipes();

        try {
            const recipes = await get(`${API_BASE}/search?keyword=${encodeURIComponent(keyword)}`);
            renderTable(recipes);
        } catch (error) {
            alert('検索中にエラーが発生しました');
        }
    });

    async function loadRecipes() {
        try {
            const recipes = await get(API_BASE);
            renderTable(recipes);
        } catch (error) {
            alert('レシピの読み込みに失敗しました');
        }
    }

    function renderTable(recipes) {
        recipeTableBody.innerHTML = '';
        recipes.forEach(recipe => {
            const tr = document.createElement('tr');

            const titleTd = document.createElement('td');
            titleTd.textContent = recipe.title;

            const caloriesTd = document.createElement('td');
            caloriesTd.textContent = (recipe.calories != null ? recipe.calories : '-') + ' kcal';

            const actionTd = document.createElement('td');

            const editBtn = document.createElement('button');
            editBtn.textContent = '編集';
            editBtn.addEventListener('click', () => editRecipe(recipe.id));

            const deleteBtn = document.createElement('button');
            deleteBtn.textContent = '削除';
            deleteBtn.addEventListener('click', () => deleteRecipe(recipe.id));

            actionTd.appendChild(editBtn);
            actionTd.appendChild(deleteBtn);

            tr.appendChild(titleTd);
            tr.appendChild(caloriesTd);
            tr.appendChild(actionTd);
            recipeTableBody.appendChild(tr);
        });
    }

    function handleApiError(errorData) {
        if (errorData.details) {
            errorData.details.forEach(d => {
                const errorEl = document.getElementById(`error-${d.field}`);
                if (errorEl) errorEl.textContent = d.message;
            });
        }
        alert(`エラー: ${errorData.message}`);
    }

    function clearErrors() {
        document.querySelectorAll('.error').forEach(el => el.textContent = '');
    }

    function resetForm() {
        document.getElementById('recipeId').value = '';
        document.getElementById('title').value = '';
        document.getElementById('description').value = '';
        document.getElementById('calories').value = '';
        document.getElementById('formTitle').textContent = 'レシピを登録';
        document.getElementById('submitBtn').textContent = '登録';
        cancelBtn.style.display = 'none';
        clearErrors();
    }

    // 編集・削除処理（renderTable の addEventListener から呼び出す）
    async function editRecipe(id) {
        try {
            const recipe = await get(`${API_BASE}/${id}`);

            document.getElementById('recipeId').value = recipe.id;
            document.getElementById('title').value = recipe.title;
            document.getElementById('description').value = recipe.description || '';
            document.getElementById('calories').value = recipe.calories || 0;

            document.getElementById('formTitle').textContent = 'レシピを編集';
            document.getElementById('submitBtn').textContent = '更新';
            cancelBtn.style.display = 'inline';
            window.scrollTo(0, 0);
        } catch (error) {
            alert('エラーが発生しました');
        }
    }

    async function deleteRecipe(id) {
        if (!confirm('削除してもよろしいですか？')) return;
        try {
            await del(`${API_BASE}/${id}`);
            loadRecipes();
        } catch (error) {
            alert('削除中にエラーが発生しました');
        }
    }

});
