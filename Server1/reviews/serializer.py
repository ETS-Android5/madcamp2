from rest_framework import serializers, viewsets
from .models import Area, Review

class AreaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Area
        fields = ('city', 'county', 'last')

class ReviewSerializer(serializers.ModelSerializer):
    area = AreaSerializer(many=False)

    class Meta:
        model = Review
        fields = ('area', 'sight', 'touch', 'taste')

    def create(self, validated_data):
        area_data = validated_data.pop('area')
        if not Area.objects.filter(
                city = area_data['city']
            ).filter(
                county = area_data['county']
            ).filter(
                last = area_data['last']
            ).exists() :
            area = Area.objects.create(**area_data)
        else:
            area = Area.objects.get(city = area_data['city'], county = area_data['county'], last = area_data['last'])

        validated_data['area'] = area
        review = Review.objects.create(**validated_data)

        return review