// モックデータの読み込み（本来はfetchなどで取得しますが、プロトタイプ用に定義を模倣します）
const mockDataPath = '../mock-data/meal-plan.json';

async function init() {
    try {
        // ローカルでの実行を想定し、データの構造を直接定義するかfetchを試みます
        // プロトタイプをブラウザですぐ確認できるよう、データを定数として扱います
        const response = await fetch(mockDataPath);
        const data = await response.json();
        renderMealPlan(data.weekly_plan);
    } catch (error) {
        console.error("データの読み込みに失敗しました。パスを確認してください。", error);
        // フォールバック用のサンプル表示ロジック
    }
}

function renderMealPlan(weeklyPlan) {
    const grid = document.getElementById('weekly-grid');
    grid.innerHTML = '';

    // 1週間分の枠を作成
    const days = ["月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日", "日曜日"];
    
    days.forEach(dayName => {
        const dayData = weeklyPlan.find(d => d.day === dayName);
        const dayColumn = document.createElement('div');
        dayColumn.className = 'flex flex-col gap-4';

        // 曜日ヘッダー
        const dateDisplay = dayData ? dayData.date.split('-').slice(1).join('/') : '--/--';
        dayColumn.innerHTML = `
            <div class="text-center font-bold pb-2 border-bottom">
                ${dayName} <span class="text-sm text-gray-400 font-normal">${dateDisplay}</span>
            </div>
        `;

        if (dayData) {
            // 朝・昼・晩のレンダリング
            const mealTypes = [
                { id: 'breakfast', label: '朝食' },
                { id: 'lunch', label: '昼食' },
                { id: 'dinner', label: '夕食' }
            ];

            mealTypes.forEach(type => {
                const meal = dayData.meals[type.id];
                const card = createMealCard(type.label, meal);
                dayColumn.appendChild(card);
            });
        } else {
            // データがない日のプレースホルダー
            dayColumn.appendChild(createEmptyState());
        }

        grid.appendChild(dayColumn);
    });
}

function createMealCard(label, meal) {
    const card = document.createElement('div');
    card.className = 'bg-white p-4 rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition-shadow cursor-pointer relative group';
    
    card.innerHTML = `
        <div class="text-xs font-bold text-emerald-500 mb-1">${label}</div>
        <div class="font-bold text-sm mb-2">${meal.title}</div>
        <div class="flex items-center justify-between mt-3">
            <span class="text-[10px] px-2 py-0.5 accent-amber text-white rounded-full font-bold">
                ${meal.total_calories} kcal
            </span>
            <div class="flex gap-1">
                <div class="w-2 h-2 rounded-full bg-blue-400" title="P"></div>
                <div class="w-2 h-2 rounded-full bg-red-400" title="F"></div>
                <div class="w-2 h-2 rounded-full bg-green-400" title="C"></div>
            </div>
        </div>
        <div class="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity flex gap-1">
            <button class="text-xs p-1 bg-gray-50 rounded">✏️</button>
            <button class="text-xs p-1 bg-gray-50 rounded">🔄</button>
        </div>
    `;
    return card;
}

function createEmptyState() {
    const div = document.createElement('div');
    div.className = 'border-2 border-dashed border-gray-200 rounded-xl p-8 flex flex-col items-center justify-center text-gray-400 hover:bg-gray-50 cursor-pointer transition-colors';
    div.innerHTML = `<span class="text-2xl mb-1">+</span><span class="text-[10px]">AI提案</span>`;
    return div;
}

// 実行
init();