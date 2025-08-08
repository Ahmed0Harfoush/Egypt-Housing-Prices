from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)  # السماح لكل المصادر

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # التأكد أن الطلب JSON
        if not request.is_json:
            return jsonify({'success': False, 'message': 'الطلب ليس JSON'}), 400

        data = request.get_json()

        # استخراج القيم
        region = data.get('region', '')
        city = data.get('city', '')
        size = float(data.get('size', 0))
        bedrooms = int(data.get('bedrooms', 0))

        # هنا يمكنك إضافة نموذج ML أو حساباتك
        prediction = (size * 1000) + (bedrooms * 5000)

        return jsonify({
            'success': True,
            'region': region,
            'city': city,
            'prediction': prediction
        }), 200

    except Exception as e:
        return jsonify({
            'success': False,
            'message': str(e)
        }), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
