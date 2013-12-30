class Location < ActiveRecord::Base
  belongs_to :user
  has_many :detections
  has_many :territories, :through => :detections

  def to_hash
    hash = Hash[self.attributes]
    hash[:location_id] = hash[:id]
    hash.delete :id
    hash
  end
end
