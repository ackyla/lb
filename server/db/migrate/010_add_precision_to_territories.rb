class AddPrecisionToTerritories < ActiveRecord::Migration
  def self.up
    change_table :territories do |t|
      t.float :precision
    end
  end

  def self.down
    change_table :territories do |t|
      t.remove :precision
    end
  end
end
