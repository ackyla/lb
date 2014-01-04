class Location < ActiveRecord::Base
  belongs_to :user
  has_many :detections
  has_and_belongs_to_many :territories

  def distance(loc)
    y1 = self.latitude * Math::PI / 180
    x1 = self.longitude * Math::PI / 180
    y2 = loc.latitude * Math::PI / 180
    x2 = loc.longitude * Math::PI / 180
    earth_r = 6378140
    deg = Math::sin(y1) * Math::sin(y2) + Math::cos(y1) * Math::cos(y2) * Math::cos(x2 - x1)
    distance = earth_r * (Math::atan(-deg / Math::sqrt(-deg * deg + 1)) + Math::PI / 2)

    return distance
  end

  def to_hash
    hash = Hash[self.attributes]
    hash[:location_id] = hash[:id]
    hash.delete :id
    hash
  end
end
